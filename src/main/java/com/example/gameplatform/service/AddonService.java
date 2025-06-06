package com.example.gameplatform.service;

import com.example.gameplatform.model.Addon;
import java.util.List;

public interface AddonService {
    List<Addon> getAllAddons();
    Addon getAddonById(Long id);
    Addon createAddon(Addon addon);
    Addon updateAddon(Long id, Addon addonDetails);
    void deleteAddon(Long id);
}