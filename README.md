![Wavepoint Logo Fade](https://cdn.modrinth.com/data/cached_images/a32cb79fecb055949174f3ef30896c2e079060c0_0.webp)
<center><h1>Wavepoint</h1>
Wavepoint is a new, simplistic waypoint plugin designed to make playing Minecraft easier. <br><br>
<a href="https://github.com/solarcosmic/Wavepoint" target="_blank">
  <img alt="github" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg">
</a>
<a href="https://papermc.io" target="_blank">
  <img alt="paper" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/paper_vector.svg">
</a>
<a href="https://purpurmc.org" target="_blank">
  <img alt="purpur" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/purpur_vector.svg">
</a>
<br><br>

![/wp set sand](https://cdn.modrinth.com/data/cached_images/c03e0e30aa7740d46fb89d465e0a6910a95f3d2d_0.webp)
![/wp list](https://cdn.modrinth.com/data/cached_images/a7f269ed64a35299c55b52d024795f071a27a29a_0.webp)
![/wp delete sand](https://cdn.modrinth.com/data/cached_images/dbe14bd0476e786e41a5434299738396d5a3a5eb_0.webp)

</center>

## Commands
Wavepoint uses one command, `/wp`, to manage waypoints:
<br><br>
![wp command](https://cdn.modrinth.com/data/cached_images/976cdd89632998f80a2e08566d76fb78599aea97_0.webp)

## Integrations
Wavepoint has the ability to integrate with some plugins to provide extra functionality:

- [Vault](https://www.spigotmc.org/resources/vault.34315/) (economy)
- [CombatLogX](https://www.spigotmc.org/resources/combatlogx.31689/) (combat logging)

```yml
# Below are integrations that work with other plugins.
# If the integration is enabled, but the plugin or requirements aren't met, nothing will happen.
integrations:
  vault:              # | https://www.spigotmc.org/resources/vault.34315/
    enabled: true       # Whether to integrate with Vault, for economy purposes.
    charge_amount: 5    # How much money to charge the user per teleport.
  combatlogx:         # | https://www.spigotmc.org/resources/combatlogx.31689/
    enabled: true       # Whether to integrate with CombatLogX.
    combat:
      # False = restricted
      set: false        # Whether to restrict setting waypoints during combat. false = restricted.
      teleport: false   # Whether to restrict teleporting to waypoints during combat. false = restricted.
```

More integrations may be added in the future.

## Permissions
Wavepoint comes with a variety of permissions that can be used with a permissions plugin like [LuckPerms](https://modrinth.com/plugin/luckperms) to limit or restrict what users can do, those being:
```yml
waypoint.wp: Allows usage of the main /wp command
waypoint.wp.tp: Allows usage of /wp tp
waypoint.wp.set: Allows usage of /wp set
waypoint.wp.list: Allows usage of /wp list
waypoint.wp.delete: Allows usage of /wp delete
waypoint.wp.info: Allows usage of /wp info
```

## Language
Wavepoint contains a language file that can be modified at any time! You can find it under `languages/en_us.yml` in the plugins folder.