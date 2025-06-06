package com.example.gameplatform.service;

import com.example.gameplatform.model.Addon;
import com.example.gameplatform.repository.AddonRepository;
import com.example.gameplatform.service.AddonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddonServiceImpl implements AddonService {

    private final AddonRepository addonRepository;

    @Autowired
    public AddonServiceImpl(AddonRepository addonRepository) {
        this.addonRepository = addonRepository;
    }

    @Override
    public List<Addon> getAllAddons() {
        return addonRepository.findAll();
    }

    @Override
    public Addon getAddonById(Long id) {
        return addonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Addon not found with id: " + id));
    }

    @Override
    public Addon createAddon(Addon addon) {
        return addonRepository.save(addon);
    }

    @Override
    public Addon updateAddon(Long id, Addon addonDetails) {
        Addon addon = getAddonById(id);
        addon.setName(addonDetails.getName());
        addon.setPrice(addonDetails.getPrice());
        addon.setType(addonDetails.getType());
        addon.setGame(addonDetails.getGame());
        return addonRepository.save(addon);
    }

    @Override
    public void deleteAddon(Long id) {
        addonRepository.deleteById(id);
    }
}