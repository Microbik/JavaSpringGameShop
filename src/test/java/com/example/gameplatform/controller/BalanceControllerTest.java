package com.example.gameplatform.controller;

import com.example.gameplatform.config.JwtUtils;
import com.example.gameplatform.dto.BalanceTopUpRequest;
import com.example.gameplatform.model.Role;
import com.example.gameplatform.model.User;
import com.example.gameplatform.repository.RoleRepository;
import com.example.gameplatform.service.BalanceService;
import com.example.gameplatform.service.CustomUserDetailsService;
import com.example.gameplatform.service.SecurityUserDetails;
import com.example.gameplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceController.class)
@AutoConfigureMockMvc
public class BalanceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private BalanceService balanceService;

	@MockBean
	private RoleRepository roleRepository;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	@MockBean
	private JwtUtils jwtUtils;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(2L);
		user.setEmail("user@example.com");
		user.setPassword("pass");
		user.setName("User");
		Role role = new Role();
		role.setName("PLAYER");
		user.setRole(role);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void topUpBalance_Success() throws Exception {

		Long userId = 2L;
		BigDecimal amount = new BigDecimal("50.00");
		BigDecimal newBalance = new BigDecimal("150.00");

		when(balanceService.topUpBalance(eq(userId), any(BigDecimal.class)))
				.thenReturn(newBalance);

		BalanceTopUpRequest request = new BalanceTopUpRequest(userId, amount);
		String requestBody = String.format("{\"userId\": %d, \"amount\": %s}", userId, amount);

		mockMvc.perform(post("/api/balance/top-up")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Balance successfully topped up by " + amount))
				.andExpect(jsonPath("$.newBalance").value(newBalance.doubleValue()))
				.andExpect(jsonPath("$.currency").value("USD"));
	}

	@Test
	@WithMockUser(username = "user@example.com", roles = "PLAYER")
	public void topUpBalance_ByOwner_Success() throws Exception {
		Long userId = 2L; // Тот же ID, что и у аутентифицированного пользователя
		BigDecimal amount = new BigDecimal("30.00");
		BigDecimal newBalance = new BigDecimal("80.00");

		when(balanceService.topUpBalance(eq(userId), any(BigDecimal.class)))
				.thenReturn(newBalance);

		String requestBody = String.format("{\"userId\": %d, \"amount\": %s}", userId, amount);

		mockMvc.perform(post("/api/balance/top-up")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Balance successfully topped up by " + amount))
				.andExpect(jsonPath("$.newBalance").value(newBalance.doubleValue()))
				.andExpect(jsonPath("$.currency").value("USD"));
	}

	@Test
	@WithMockUser(username = "user@example.com", roles = "PLAYER")
	public void topUpBalance_UnauthorizedUser_AccessDenied() throws Exception {
		Long userId = 1L; // Чужой ID
		BigDecimal amount = new BigDecimal("20.00");

		String requestBody = String.format("{\"userId\": %d, \"amount\": %s}", userId, amount);

		mockMvc.perform(post("/api/balance/top-up")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void topUpBalance_InvalidAmount_BadRequest() throws Exception {
		Long userId = 2L;
		BigDecimal amount = new BigDecimal("-10.00"); // Неверная сумма

		when(balanceService.topUpBalance(eq(userId), any(BigDecimal.class)))
				.thenThrow(new IllegalArgumentException("Amount must be positive"));

		String requestBody = String.format("{\"userId\": %d, \"amount\": %s}", userId, amount);

		mockMvc.perform(post("/api/balance/top-up")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error").value("Amount must be positive"));
	}

	@Test
	@WithMockUser(username = "user@example.com")
	public void getCurrentBalance_Success() throws Exception {
		Long userId = 2L;
		BigDecimal balance = new BigDecimal("100.00");

		when(balanceService.getCurrentBalance(userId)).thenReturn(balance);

		mockMvc.perform(get("/api/balance/current"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(balance.doubleValue()));
	}

	@Test
	public void getCurrentBalance_Unauthenticated_Forbidden() throws Exception {
		mockMvc.perform(get("/api/balance/current"))
				.andExpect(status().isForbidden());
	}
}


