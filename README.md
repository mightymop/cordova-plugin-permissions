# Android:

### 1. Add plugin
cordova plugin add https://github.com/mightymop/cordova-plugin-permissions.git
### 2. For Typescript add following code to main ts file: 
/// &lt;reference types="cordova-plugin-permissions" /&gt;<br/>

### 3. Before build:
install cordova-plugin-nslookup (https://github.com/mightymop/cordova-plugin-permissions.git)
add to config.xml > platform > android



### 4. Usage:

methods:

```
	var permissions: string[] = ['<permissionstring>'];
	window.permissions.check(permissions,success,err);
	
	
	var permissions: string[] = ['<permissionstring>'];
	window.permissions.ask(permissions,success,err);	
	
```
