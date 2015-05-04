<img src="http://xericore.com/files/img/EasyCarts_Logo_V2.png" alt="EasyCarts Logo">
<br><b>Travel quickly and easily with minecarts!</b>

<p>
EasyCarts is a Bukkit/Spigot plugin for Minecraft Servers that enables hassle-free and reliable 
transportation for Minecarts with players in them. 
This plugin has been tested with Spigot 1.8, but it should work on older Bukkit versions as well. 
</p>
<b>Important note</b>: In order for intersections to work correctly, they must be built with straight rails only (like a <b>+</b>). Building them with a curve in the center will disable minecarts from stopping at intersections. <b>T</b>-Intersections work as well.
<br>
<img src="http://kratanien.com/wp-content/uploads/2015/02/easycarts_intersections.png" alt="Intersections that work with EasyCarts." width="33%" height="33%" />
<br>
<br>No special blocks are needed to stop the cart at intersections though.
<br>EasyCarts doesn't affect Carts with Chests, Furnaces, Hoppers TNT or mobs in them.
<br>
<br><b>Hint:</b> EasyCarts has no slowdown blocks. If you want to slow down your cart on a certain part of the track and then boost it again, build the following rails:
<img src="http://kratanien.com/wp-content/uploads/2015/02/slowrails.png" alt="Rails setup that slows down the cart." />

<h4>Features:</h4>
All speed values can be changed in the <i>config.yml</i>.
<ul>
  <li>Minecarts will stop at intersections. Continue simply by looking in the desired direction and pressing forward.</li>
  <li>Minecarts will automatically slow down before curves or slopes to avoid derailing or crashing into upward slope block. Carts speed up again after curve or slope.</li>
  <li>Players can ride the minecart faster even without booster blocks.</li>
  <li>Increases maximum speed of minecarts.</li>
  <li>Powered Rails can boost the minecart more.</li>
  <li>Ops can change the speed values via commands.</li>
  <li>Minecarts will push entities on a collision course out of the way to avoid collision.</li>
</ul>

<h4>Commands:</h4>
<ul>
  <li><b>/easycarts reload: </b>Reloads the settings from <i>config.yml</i> to memory. Useful for testing values ingame on a server.</li> 
  <li><b>/easycarts push: </b>Sets the value of <b>MaxPushSpeedPercent</b> in <i>config.yml</i> and saves config.</li>
  <li><b>/easycarts boost: </b>Sets the value of <b>PoweredRailBoostPercent</b> in <i>config.yml</i> and saves config.</li>
  <li><b>/easycarts maxspeed: </b>Sets the value of <b>MaxPushSpeedPercent</b> in <i>config.yml</i> and saves config.</li>
  <li><b>/easycarts slowwhenempty: </b>Toggles the value of <b>SlowWhenEmpty</b> in <i>config.yml</i> and saves config.</li>
</ul>
Refer to <i>config.yml</i> comments for more infos on the parameters.

<h4>Permissions:</h4>
<b>easycarts.admin</b>: Enables execution of the above commands. Granted to <i>OPs</i> by default. 

<h4>Known caveats:</h4>
<ul>
  <li>Carts will derail after start if spawned directly on a curve.</li>
  <li>Carts will only stop at intersections with flat rails. Curves or slopes intersections were not implemented on purpose.</li>
</ul>

<br>
I highly recommend installing CraftBook and enabling the <b>MinecartTemporaryCart</b> feature. This allows you to right click a rail with an empty hand to instantly spawn a minecart. Just make sure to set <i>RemoveMinecartOnExit: true</i> in <i>config.yml</i> to correctly remove carts when doing so.
<br>
Thanks also to Surfdudeboy as his forum post <a href="https://bukkit.org/threads/any-tips-on-preventing-minecart-derail-during-high-speed-turns.274365/">here</a> helped me solve an issue.

<br><b>Have fun with the plugin!</b>
