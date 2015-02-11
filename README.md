# EasyCarts
<b>Travel quickly and easily using Minecarts!</b>

<p>
EasyCarts is a Bukkit/Spigot plugin for Minecraft Servers that enables hassle-free and reliable 
transportation for Minecarts with players in them. 
This plugin has been tested with Spigot 1.8, but it should work on older Bukkit versions as well. 
</p>
<b>Important note</b>: In order for intersections to work correctly, they must be built with straight rails only (like a <b>+</b>). Building them with a curve in the center will disable minecarts from stopping at intersections. <b>T</b>-Intersections work as well.
<br>No special blocks are needed to stop the cart at intersections though.

<h4>Features:</h4>
All speed values can be changed in the <i>config.yml</i>.
<ul>
  <li>Minecarts will stop at intersections. Continue simply by looking in the desired direction and pressing forward.</li>
  <li>Minecarts will automatically slow down before curves or slopes to avoid derailing or crashing into upward slope block. Carts speed up again after curve or slope.</li>
  <li>Players can ride the minecart faster even without booster blocks.</li>
  <li>Increases maximum speed of minecarts.</li>
  <li>Powered Rails can boost the minecart more.</li>
  <li>Ops can change the speed values via commands.</li>
</ul>

<h4>Commands:</h4>
<ul>
  <li><b>/easycarts reload: </b>Reloads the settings from <i>config.yml</i> to memory. Useful for testing values ingame on a server.</li> 
  <li><b>/easycarts push: </b>Sets the value of <b>MaxPushSpeedPercent</b> in <i>config.yml</i> and saves config.</li>
  <li><b>/easycarts boost: </b>Sets the value of <b>PoweredRailBoostPercent</b> in <i>config.yml</i> and saves config.</li>
  <li><b>/easycarts maxspeed: </b>Sets the value of <b>MaxPushSpeedPercent</b> in <i>config.yml</i> and saves config.</li>
</ul>
Refer to <i>config.yml</i> comments for more infos on the parameters.

<h4>Permissions:</h4>
<b>easycarts.admin</b>: Enables execution of the above commands. Granted to <i>OPs</i> by default. 