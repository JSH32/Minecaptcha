![Banner](https://i.imgur.com/cWTW26y.png)

## Minecaptcha

Minecaptcha is a special plugin that keeps bots away from your server. There is a map which is given to the player on join, this map has a code on it. The user has to enter this code correctly in the chat, once this happens they will be sent to a server of your choice with bungeecord. If they get it wrong they will be kicked from the server. There is even an option to make a time limit, this will show on the map aswell.

## Config

```
prefix: '[&aCaptcha&r]'
tries: 3
map_in_offhand: 'false'
time_limit_enabled: 'false'
time_limit: 10
success_server: 
messages:
  success: Captcha &asolved!
  retry: Captcha &efailed, &rplease try again. ({CURRENT}/{MAX})
  fail: Captcha &cfailed!

```
## About
This plugin was created for the `lewd.world` anarchy server in 2020
It currently supports 1.15.2
