/**
 *  Copyright 2015 Stuart Buchanan
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Tado Thermostat
 *
 *	Author: Stuart Buchanan, Based on original work by Ian M with thanks
 *	Date: 2016-04-05 v2.0 Further Changes to MultiAttribute Tile
 *	Date: 2016-04-05 v1.9 Amended Device Handler Name
 *	Date: 2016-04-05 v1.8 Added all thermostat related capabilities
 *  Date: 2016-04-05 v1.7 Amended device to be capable of both Fahrenheit and celsius and amended the Device multiattribute tile
 *  Date: 2016-04-05 v1.6 switched API calls to new v2 calls as the old ones had been deprecated.
 *  Date: 2016-02-21 v1.5 switched around thermostatOperatingState & thermostatMode to get better compatibility with Home Remote
 *  Date: 2016-02-21 v1.4 added HeatingSetPoint & CoolingSetPoint to make compatible with SmartTiles
 *  Date: 2016-02-21 v1.3 amended the read thermostat properties to match the ST Thermostat Capability
 *  Date: 2016-02-14 v1.2 amended the thermostat properties to match the ST Capability.Thermostat values
 *  Date: 2016-01-23 v1.1 fixed error in Tado Mode detection
 *	Date: 2016-01-22 v1.1 Add Heating & Cooling Controls (initial offering, will need to look into adding all possible commands)
 *	Date: 2015-12-04 v1.0 Initial Release With Temperatures & Relative Humidity
 */
 
import groovy.json.JsonOutput

preferences {
	input("username", "text", title: "Username", description: "Your Tado username")
	input("password", "password", title: "Password", description: "Your Tado password")
    //input("tempunit", "enum", title: "Temperature Unit to Use (Celcius or Fahrenheit)?", options: ["C","F"], required: false, defaultValue: "C")
}  
 
metadata {
	definition (name: "Tado Cooling Thermostat", namespace: "fuzzysb", author: "Stuart Buchanan") {
		capability "Actuator"
        capability "Temperature Measurement"
		capability "Thermostat Cooling Setpoint"
		capability "Thermostat Heating Setpoint"
		capability "Thermostat Mode"
		capability "Thermostat Fan Mode"
		capability "Thermostat Setpoint"
		capability "Thermostat Operating State"
		capability "Thermostat"
		capability "Relative Humidity Measurement"
		capability "Polling"
		capability "Refresh"
        
        command "heatingSetpointUp"
        command "heatingSetpointDown"
        command "coolingSetpointUp"
        command "coolingSetpointDown"
        command "dry"
        command "on"
        
	}

	// simulator metadata
	simulator {
		// status messages

		// reply messages
	}

	tiles(scale: 2){
      	multiAttributeTile(name: "thermostat", type:"thermostat", width:6, height:4) {
			tileAttribute("device.temperature", key:"PRIMARY_CONTROL", canChangeIcon: true, canChangeBackground: true){
            	attributeState "default", label:'${currentValue}�', backgroundColor:"#fab907", icon:"st.Home.home1"
            }
			tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'${currentValue}%', unit:"%")
  			}
            tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
    			attributeState("SLEEP", label:'${name}', backgroundColor:"#0164a8")
    			attributeState("HOME", label:'${name}', backgroundColor:"#fab907")
    			attributeState("AWAY", label:'${name}', backgroundColor:"#62aa12")
                attributeState("OFF", label:'${name}', backgroundColor:"#c0c0c0")
                attributeState("MANUAL", label:'${name}', backgroundColor:"#804000")
		}
 	}
        
        standardTile("thermostatOperatingState", "device.thermostatOperatingState", width: 2, height: 2, canChangeIcon: true, canChangeBackground: true) {         
			state("SLEEP", label:'${name}', backgroundColor:"#0164a8", icon:"st.Bedroom.bedroom2")
            state("HOME", label:'${name}', backgroundColor:"#fab907", icon:"st.Home.home2")
            state("AWAY", label:'${name}', backgroundColor:"#62aa12", icon:"st.Outdoor.outdoor18")
            state("OFF", label:'${name}', backgroundColor:"#ffffff", icon:"st.switches.switch.off", defaultState: true)
            state("MANUAL", label:'${name}', backgroundColor:"#804000", icon:"st.Weather.weather1")
		}
    	
        standardTile("refresh", "device.switch", inactiveLabel: false, width: 2, height: 1, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        
        standardTile("thermostatMode", "device.thermostatMode", width: 2, height: 2, canChangeIcon: true, canChangeBackground: true) {
        	state("HEAT", label:'${name}', backgroundColor:"#ea2a2a", icon:"st.Weather.weather14")
            state("COOL", label:'${name}', backgroundColor:"#089afb", icon:"st.Weather.weather7")
            state("DRY", label:'${name}', backgroundColor:"#ab7e13", icon:"st.Weather.weather12")
            state("FAN", label:'${name}', backgroundColor:"#ffffff", icon:"st.Appliances.appliances11")
            state("AUTO", label:'${name}', backgroundColor:"#62aa12", icon:"st.Electronics.electronics13")
            state("OFF", label:'${name}', backgroundColor:"#ffffff", icon:"st.switches.switch.off", defaultState: true)  
		}
        
		valueTile("thermostatSetpoint", "device.thermostatSetpoint", width: 2, height: 1, decoration: "flat") {
			state "default", label: 'Set Point\r\n\${currentValue}�'
		}
        
        valueTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 1, decoration: "flat") {
			state "default", label: 'Set Point\r\n\${currentValue}�'
		}
        
        valueTile("coolingSetpoint", "device.coolingSetpoint", width: 2, height: 1, decoration: "flat") {
			state "default", label: 'Set Point\r\n\${currentValue}�'
		}
		
		valueTile("outsidetemperature", "device.outsidetemperature", width: 2, height: 1, decoration: "flat") {
			state "outsidetemperature", label: 'Outside Temp\r\n${currentValue}�'
		}
       
		standardTile("thermostatFanMode", "device.thermostatFanMode", width: 2, height: 2, canChangeIcon: true, canChangeBackground: true) {
        	state("OFF", label:'${name}', backgroundColor:"#ffffff", icon:"st.Appliances.appliances11", defaultState: true)
            state("AUTO", label:'${name}', backgroundColor:"#ffffff", icon:"st.Appliances.appliances11")
            state("HIGH", label:'${name}', backgroundColor:"#ffffff", icon:"st.Appliances.appliances11")
            state("MIDDLE", label:'${name}', backgroundColor:"#ffffff", icon:"st.Appliances.appliances11")
            state("LOW", label:'${name}', backgroundColor:"#ffffff", icon:"st.Appliances.appliances11")       
		}
		standardTile("setAuto", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"Auto", action:"thermostat.auto"
		}
        standardTile("setDry", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"Dry", action:"dry"
		}
        standardTile("setOn", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"On", action:"on"
		}
        standardTile("setOff", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"Off", action:"thermostat.off"
		}
        standardTile("cool", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"Cool", action:"thermostat.cool", backgroundColor:"#0683ff"
		}
        standardTile("heat", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"Heat", action:"thermostat.heat", backgroundColor:"#bc2323"
		}
        standardTile("emergencyHeat", "device.thermostat", width: 2, height: 2, decoration: "flat") {
			state "default", label:"EMERGENCY\r\nHEAT", action:"thermostat.emergencyHeat", backgroundColor:"#bc2323"
		}
        standardTile("fan", "device.thermostat", width: 2, height: 1, decoration: "flat") {
			state "default", label:"Fan", action:"thermostat.fanAuto"
		}
        standardTile("coolingSetpointUp", "device.coolingSetpoint", canChangeIcon: false, decoration: "flat") {
            state "coolingSetpointUp", label:'  ', action:"coolingSetpointUp", icon:"st.thermostat.thermostat-up", backgroundColor:"#0683ff"
        }
		standardTile("coolingSetpointDown", "device.coolingSetpoint", canChangeIcon: false, decoration: "flat") {
            state "coolingSetpointDown", label:'  ', action:"coolingSetpointDown", icon:"st.thermostat.thermostat-down", backgroundColor:"#0683ff"
        }
		standardTile("heatingSetpointUp", "device.heatingSetpoint", canChangeIcon: false, decoration: "flat") {
            state "heatingSetpointUp", label:'  ', action:"heatingSetpointUp", icon:"st.thermostat.thermostat-up", backgroundColor:"#bc2323"
        }
        standardTile("heatingSetpointDown", "device.heatingSetpoint", canChangeIcon: false, decoration: "flat") {
            state "heatingSetpointDown", label:'  ', action:"heatingSetpointDown", icon:"st.thermostat.thermostat-down", backgroundColor:"#bc2323"
        }
		
		main(["thermostat"])
		details(["thermostat","thermostatMode","coolingSetpointUp","coolingSetpointDown","autoOperation","heatingSetpointUp","heatingSetpointDown","outsidetemperature","thermostatSetpoint","thermostatOperatingState","refresh","thermostatFanMode","setAuto","setOn","setOff","fan","cool","heat","setDry"])
	}
}

// Parse incoming device messages to generate events
private parseMeResponse(resp) {
    log.debug("Executing parseMeResponse: "+resp.data)
    log.debug("Output status: "+resp.status)
    if(resp.status == 200) {
    	log.debug("Executing parseMeResponse.successTrue")
        state.homeId = resp.data.homes[0].id
        log.debug("Got HomeID Value: " + state.homeId)
        
    }else if(resp.status == 201){
        log.debug("Something was created/updated")
    }
}

private parseputResponse(resp) {
	log.debug("Executing parseputResponse: "+resp.data)
    log.debug("Output status: "+resp.status)
}

private parseResponse(resp) {    
    log.debug("Executing parseResponse: "+resp.data)
    log.debug("Output status: "+resp.status)
	def temperatureUnit = state.tempunit
    log.debug("Temperature Unit is ${temperatureUnit}")
	def humidityUnit = "%"
    def ACMode
    def ACFanSpeed
    def thermostatSetpoint
    if(resp.status == 200) {
        log.debug("Executing parseResponse.successTrue")
        def temperature
        if (temperatureUnit == "C") {
        	temperature = (Math.round(resp.data.sensorDataPoints.insideTemperature.celsius *10 ) / 10)
        }
        else if(temperatureUnit == "F"){
        	temperature = (Math.round(resp.data.sensorDataPoints.insideTemperature.fahrenheit * 10) / 10)
        }
        log.debug("Read temperature: " + temperature)
        sendEvent(name: 'temperature', value: temperature, unit: temperatureUnit)
        log.debug("Send Temperature Event Fired")
        def autoOperation = "OFF"
        if(resp.data.overlayType == null){
        	autoOperation = resp.data.tadoMode
        }
        else if(resp.data.overlayType == "NO_FREEZE"){
        	autoOperation = "OFF"
        }else if(resp.data.overlayType == "MANUAL"){
        	autoOperation = "MANUAL"
        }
        log.debug("Read thermostatOperatingState: " + autoOperation)
        sendEvent(name: 'thermostatOperatingState', value: autoOperation)
        log.debug("Send thermostatMode Event Fired")

        def humidity 
        if (resp.data.sensorDataPoints.humidity.percentage != null){
        	humidity = resp.data.sensorDataPoints.humidity.percentage
        }else{
        	humidity = "--"
        }
        log.debug("Read humidity: " + humidity)
			       
        sendEvent(name: 'humidity', value: humidity,unit: humidityUnit)

    	if (resp.data.setting.power == "OFF"){
       		ACMode = "OFF"
        	log.debug("Read thermostatMode: " + ACMode)
			ACFanSpeed = "OFF"
        	log.debug("Read thermostatFanMode: " + ACFanSpeed)
			thermostatSetpoint = "0"
        	log.debug("Read thermostatSetpoint: " + thermostatSetpoint)
    	}
   	 	else if (resp.data.setting.power == "ON"){
       		ACMode = resp.data.setting.mode
			log.debug("thermostatMode: " + ACMode)
			ACFanSpeed = resp.data.setting.fanSpeed
        	log.debug("Read thermostatFanMode: " + ACFanSpeed)
        if (ACMode == "DRY" || ACMode == "AUTO" || ACMode == "FAN"){
        	thermostatSetpoint = "--"
        }else{
       		if (temperatureUnit == "C") {
        		thermostatSetpoint = Math.round(resp.data.setting.temperature.celsius)
        	}
        	else if(temperatureUnit == "F"){
        		thermostatSetpoint = Math.round(resp.data.setting.temperature.fahrenheit)
        	}
        }
        log.debug("Read thermostatSetpoint: " + thermostatSetpoint)
    	}
	}	

	else{
        log.debug("Executing parseResponse.successFalse")
    }
    sendEvent(name: 'thermostatFanMode', value: ACFanSpeed)
    log.debug("Send thermostatFanMode Event Fired")
	sendEvent(name: 'thermostatMode', value: ACMode)
    log.debug("Send thermostatMode Event Fired")
    sendEvent(name: 'thermostatSetpoint', value: thermostatSetpoint, unit: temperatureUnit)
    log.debug("Send thermostatSetpoint Event Fired")
    sendEvent(name: 'heatingSetpoint', value: thermostatSetpoint, unit: temperatureUnit)
    log.debug("Send heatingSetpoint Event Fired")
    sendEvent(name: 'coolingSetpoint', value: thermostatSetpoint, unit: temperatureUnit)
    log.debug("Send coolingSetpoint Event Fired")
	

}

private parseTempResponse(resp) {
    log.debug("Executing parseTempResponse: "+resp.data)
    log.debug("Output status: "+resp.status)
    if(resp.status == 200) {
    	log.debug("Executing parseTempResponse.successTrue")
        def tempunitname = resp.data.temperatureUnit
        if (tempunitname == "CELSIUS") {
        	log.debug("Setting Temp Unit to C")
        	state.tempunit = "C"
        }
        else if(tempunitname == "FAHRENHEIT"){
        	log.debug("Setting Temp Unit to F")
        	state.tempunit = "F"
        }       
    }else if(resp.status == 201){
        log.debug("Something was created/updated")
    }
}



private parseCapabilitiesResponse(resp) {
    log.debug("Executing parseCapabilitiesResponse: "+resp.data)
    log.debug("Output status: " + resp.status)
    if(resp.status == 200) {
    	try{
    	log.debug("Executing parseResponse.successTrue")
       	state.tadoType = resp.data.type
        log.debug("Tado Type is ${state.tadoType}")
        if(resp.data.AUTO || (resp.data.AUTO).toString() == "[:]"){
        	log.debug("settingautocapability state true")
        	state.supportsAuto = "true"
        } else {
        	log.debug("settingautocapability state false")
        	state.supportsAuto = "false"
        }
        if(resp.data.COOL || (resp.data.COOL).toString() == "[:]"){
        	log.debug("setting COOL capability state true")
        	state.supportsCool = "true"
            def coolfanmodelist = resp.data.COOL.fanSpeeds
            if(coolfanmodelist.find { it == 'AUTO' }){
            	log.debug("setting COOL Auto Fan Speed capability state true")
            	state.SupportsCoolAutoFanSpeed = "true"
            } else {
            	log.debug("setting COOL Auto Fan Speed capability state false")
            	state.SupportsCoolAutoFanSpeed = "false"
            }
            if (state.tempunit == "C"){
            	state.MaxCoolTemp = resp.data.COOL.temperatures.celsius.max
                log.debug("set state.MaxCoolTemp to : " + state.MaxCoolTemp + "C")
                state.MinCoolTemp = resp.data.COOL.temperatures.celsius.min
                log.debug("set state.MinCoolTemp to : " + state.MinCoolTemp + "C")
            } else if (state.tempunit == "F") {
            	state.MaxCoolTemp = resp.data.COOL.temperatures.fahrenheit.max
                log.debug("set state.MaxCoolTemp to : " + state.MaxCoolTemp + "F")
                state.MinCoolTemp = resp.data.COOL.temperatures.fahrenheit.min
                log.debug("set state.MinCoolTemp to : " + state.MinCoolTemp + "F")
           	}    
        } else {
        	log.debug("setting COOL capability state false")
        	state.supportsCool = "false"
        }
        if(resp.data.DRY || (resp.data.DRY).toString() == "[:]"){
        	log.debug("setting DRY capability state true")
        	state.supportsDry = "true"
        } else {
        	log.debug("setting DRY capability state false")
        	state.supportsDry = "false"
        }
        if(resp.data.FAN || (resp.data.FAN).toString() == "[:]"){
        	log.debug("setting FAN capability state true")
        	state.supportsFan = "true"
        } else {
        	log.debug("setting FAN capability state false")
        	state.supportsFan = "false"
        }
        if(resp.data.HEAT || (resp.data.HEAT).toString() == "[:]"){
        	log.debug("setting HEAT capability state true")
        	state.supportsHeat = "true"
            def heatfanmodelist = resp.data.HEAT.fanSpeeds
            if(heatfanmodelist.find { it == 'AUTO' }){
            	log.debug("setting HEAT Auto Fan Speed capability state true")
            	state.SupportsHeatAutoFanSpeed = "true"
            } else {
            	log.debug("setting HEAT Auto Fan Speed capability state false")
            	state.SupportsHeatAutoFanSpeed = "false"
            }
            if (state.tempunit == "C"){
            	state.MaxHeatTemp = resp.data.HEAT.temperatures.celsius.max
                log.debug("set state.MaxHeatTemp to : " + state.MaxHeatTemp + "C")
                state.MinHeatTemp = resp.data.HEAT.temperatures.celsius.min
                log.debug("set state.MinHeatTemp to : " + state.MinHeatTemp + "C")
            } else if (state.tempunit == "F") {
            	state.MaxHeatTemp = resp.data.HEAT.temperatures.fahrenheit.max
                log.debug("set state.MaxHeatTemp to : " + state.MaxHeatTemp + "F")
                state.MinHeatTemp = resp.data.HEAT.temperatures.fahrenheit.min
                log.debug("set state.MinHeatTemp to : " + state.MinHeatTemp + "F")
           	}    
        } else {
        	log.debug("setting HEAT capability state false")
        	state.supportsHeat = "false"
        }
        log.debug("state.supportsDry = ${state.supportsDry}")
        log.debug("state.supportsCool = ${state.supportsCool}")
        log.debug("state.supportsFan = ${state.supportsFan}")
        log.debug("state.supportsAuto = ${state.supportsAuto}")
        log.debug("state.supportsHeat = ${state.supportsHeat}")
    }catch(Exception e){
        log.debug("___exception: " + e)
    }   
    }else if(resp.status == 201){
        log.debug("Something was created/updated")
    }
}


private parseweatherResponse(resp) {
    log.debug("Executing parseweatherResponse: "+resp.data)
    log.debug("Output status: "+resp.status)
	def temperatureUnit = state.tempunit
    log.debug("Temperature Unit is ${temperatureUnit}")
    if(resp.status == 200) {
    	log.debug("Executing parseResponse.successTrue")
        def outsidetemperature
        if (temperatureUnit == "C") {
        	outsidetemperature = resp.data.outsideTemperature.celsius
        }
        else if(temperatureUnit == "F"){
        	outsidetemperature = resp.data.outsideTemperature.fahrenheit
        }
        log.debug("Read outside temperature: " + outsidetemperature)
        sendEvent(name: 'outsidetemperature', value: outsidetemperature , unit: temperatureUnit)
        log.debug("Send Outside Temperature Event Fired")
        
    }else if(resp.status == 201){
        log.debug("Something was created/updated")
    }
}

def updated(){
def cmds = [
getidCommand(),
getTempUnitCommand(),
getCapabilitiesCommand()
]
delayBetween(cmds, 2000)
}

def installed(){
def cmds = [
getidCommand(),
getTempUnitCommand(),
getCapabilitiesCommand(),
refresh()
]
delayBetween(cmds, 2000)
}

def poll() {
	log.debug "Executing 'poll'"
	refresh()
}

def refresh() {
	log.debug "Executing 'refresh'"
    statusCommand()
    weatherStatusCommand()
    
}

def auto() {
	log.debug "Executing 'auto'"
	autoCommand()
    refresh()
}

def on() {
	log.debug "Executing 'on'"
	onCommand()
    refresh()
}

def off() {
	log.debug "Executing 'off'"
	offCommand()
    refresh()
}

def dry() {
	log.debug "Executing 'dry'"
	dryCommand()
    refresh()
}

def setThermostatMode(requiredMode){
	switch (requiredMode) {
    	case "DRY":
        	dry()
        break
    	case "HEAT":
        	heat()
        break
        case "COOL":
        	cool()
        break
        case "AUTO":
        	auto()
        break
        case "FAN":
        	fanAuto()
        break
     }
}

def setHeatingSetpoint(targetTemperature) {
	log.debug "Executing 'setHeatingSetpoint'"
    log.debug "Target Temperature ${targetTemperature}"
    setHeatingTempCommand(targetTemperature)
	refresh()
}

def heatingSetpointUp(){
	log.debug "Current SetPoint Is " + (device.currentValue("thermostatSetpoint")).toString()
    if ((device.currentValue("thermostatSetpoint").toInteger() - 1 ) < state.MinHeatTemp){
    	log.debug("cannot decrease heat setpoint, its already at the minimum level of " + state.MinHeatTemp)
    } else {
		int newSetpoint = (device.currentValue("thermostatSetpoint")).toInteger() + 1
		log.debug "Setting heatingSetpoint up to: ${newSetpoint}"
		setHeatingSetpoint(newSetpoint)
    }
}

def heatingSetpointDown(){
	log.debug "Current SetPoint Is " + (device.currentValue("thermostatSetpoint")).toString()
    if ((device.currentValue("thermostatSetpoint").toInteger() + 1 ) > state.MaxHeatTemp){
    	log.debug("cannot increase heat setpoint, its already at the maximum level of " + state.MaxHeatTemp)
    } else {
		int newSetpoint = (device.currentValue("thermostatSetpoint")).toInteger() - 1
		log.debug "Setting heatingSetpoint down to: ${newSetpoint}"
		setHeatingSetpoint(newSetpoint)
    }
}

def setCoolingSetpoint(targetTemperature) {
	log.debug "Executing 'setCoolingSetpoint'"
    log.debug "Target Temperature ${targetTemperature}"
    setCoolingTempCommand(targetTemperature)
	refresh()
}

def coolingSetpointUp(){
	log.debug "Current SetPoint Is " + (device.currentValue("thermostatSetpoint")).toString()
    if ((device.currentValue("thermostatSetpoint").toInteger() + 1 ) > state.MaxCoolTemp){
    	log.debug("cannot increase cool setpoint, its already at the maximum level of " + state.MaxCoolTemp)
    } else {
		int newSetpoint = (device.currentValue("thermostatSetpoint")).toInteger() + 1
		log.debug "Setting coolingSetpoint up to: ${newSetpoint}"
		setCoolingSetpoint(newSetpoint)
    }
}

def coolingSetpointDown(){
	log.debug "Current SetPoint Is " + (device.currentValue("thermostatSetpoint")).toString()
    if ((device.currentValue("thermostatSetpoint").toInteger() - 1 ) < state.MinCoolTemp){
    	log.debug("cannot decrease cool setpoint, its already at the minimum level of " + state.MinCoolTemp)
    } else {
		int newSetpoint = (device.currentValue("thermostatSetpoint")).toInteger() - 1
		log.debug "Setting coolingSetpoint down to: ${newSetpoint}"
		setCoolingSetpoint(newSetpoint)
    }
}

private sendCommand(method, args = []) {
    def methods = [
		'getid': [
        			uri: "https://my.tado.com", 
                    path: "/api/v2/me", 
                    requestContentType: "application/json", 
                    query: [username:settings.username, password:settings.password]
                    ],
        'gettempunit': [
        			uri: "https://my.tado.com", 
                    path: "/api/v2/homes/${state.homeId}", 
                    requestContentType: "application/json", 
                    query: [username:settings.username, password:settings.password]
                    ],
        'getcapabilities': [
        			uri: "https://my.tado.com", 
                    path: "/api/v2/homes/" + state.homeId + "/zones/1/capabilities", 
                    requestContentType: "application/json", 
                    query: [username:settings.username, password:settings.password]
                    ],
        'status': [
        			uri: "https://my.tado.com", 
                    path: "/api/v2/homes/" + state.homeId + "/zones/1/state", 
                    requestContentType: "application/json", 
                    query: [username:settings.username, password:settings.password]
                    ],
		'temperature': [	
        			uri: "https://my.tado.com",
        			path: "/api/v2/homes/" + state.homeId + "/zones/1/overlay",
        			requestContentType: "application/json",
                    query: [username:settings.username, password:settings.password],
                  	body: args[0]
                   	],
		'weatherStatus': [	
        			uri: "https://my.tado.com",
        			path: "/api/v2/homes/" + state.homeId + "/weather",
        			requestContentType: "application/json",
    				query: [username:settings.username, password:settings.password]
                   	]
	]

	def request = methods.getAt(method)
    
    log.debug "Http Params ("+request+")"
    
    try{
        log.debug "Executing 'sendCommand'"
        
        if (method == "getid"){
            httpGet(request) { resp ->            
                parseMeResponse(resp)
            }
        }else if (method == "gettempunit"){
            httpGet(request) { resp ->            
                parseTempResponse(resp)
            }
       	}else if (method == "getcapabilities"){
            httpGet(request) { resp ->            
                parseCapabilitiesResponse(resp)
            }
        }else if (method == "status"){
            httpGet(request) { resp ->            
                parseResponse(resp)
            }
		}else if (method == "temperature"){
            httpPut(request) { resp ->            
                parseputResponse(resp)
            }
        }else if (method == "weatherStatus"){
            log.debug "calling weatherStatus Method"
            httpGet(request) { resp ->            
                parseweatherResponse(resp)
            }
        }else{
            httpGet(request)
        }
    } catch(Exception e){
        log.debug("___exception: " + e)
    }
}



// Commands to device
def getidCommand(){
	log.debug "Executing 'sendCommand.getidCommand'"
	sendCommand("getid",[])
}

def getCapabilitiesCommand(){
	log.debug "Executing 'sendCommand.getcapabilities'"
	sendCommand("getcapabilities",[])
}

def getTempUnitCommand(){
	log.debug "Executing 'sendCommand.getidCommand'"
	sendCommand("gettempunit",[])
}

def autoCommand(){
	log.debug "Executing 'sendCommand.autoCommand'"
	def jsonbody = new groovy.json.JsonOutput().toJson([setting:[mode:"AUTO", power:"ON", type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def dryCommand(){
	log.debug "Executing 'sendCommand.dryCommand'"
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[mode:"DRY", power:"ON", type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def fanAuto(){
	log.debug "Executing 'sendCommand.fanAutoCommand'"
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[mode:"FAN", power:"ON", type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def fanOn(){
	fanAuto()
}

def fanCirculate(){
	fanAuto()
}

def cool(){
	coolCommand()
}

def heat(){
	heatCommand()
}


def setCoolingTempCommand(targetTemperature){
    def supportedfanspeed
    def jsonbody
    if (state.SupportsCoolAutoFanSpeed == "true"){
    	supportedfanspeed = "AUTO"
    } else {
        supportedfanspeed = "HIGH"
    }  
 	if (state.tempunit == "C") {
        	jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"COOL", power:"ON", temperature:[celsius:targetTemperature], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
        }
        else if(state.tempunit == "F"){
            jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"COOL", power:"ON", temperature:[fahrenheit:targetTemperature], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
        }
	log.debug "Executing 'sendCommand.setCoolingTempCommand' to ${targetTemperature}"
	sendCommand("temperature",[jsonbody])
}

def setHeatingTempCommand(targetTemperature){
    def supportedfanspeed
    def jsonbody
    if (state.SupportsHeatAutoFanSpeed == "true"){
    	supportedfanspeed = "AUTO"
        } else {
        supportedfanspeed = "HIGH"
        }  
 	if (state.tempunit == "C") {
        	jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"HEAT", power:"ON", temperature:[celsius:targetTemperature], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
        }
        else if(state.tempunit == "F"){
        	jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"HEAT", power:"ON", temperature:[fahrenheit:targetTemperature], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
        }
	log.debug "Executing 'sendCommand.setHeatingTempCommand' to ${targetTemperature}"
	sendCommand("temperature",[jsonbody])
}

def offCommand(){
	log.debug "Executing 'sendCommand.offCommand'"
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[type:"AIR_CONDITIONING", power:"OFF"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def onCommand(){
	log.debug "Executing 'sendCommand.onCommand'"
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:"AUTO", mode:"COOL", power:"ON", temperature:[celsius:21], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def coolCommand(){
	log.debug "Executing 'sendCommand.coolCommand'"
    def initialsetpointtemp
    def supportedfanspeed
    if (state.SupportsCoolAutoFanSpeed == "true"){
    	supportedfanspeed = "AUTO"
        } else {
        supportedfanspeed = "HIGH"
        }  
    if(device.currentValue("thermostatSetpoint") == 0){
    	initialsetpointtemp = 21
    } else {
    	initialsetpointtemp = device.currentValue("thermostatSetpoint")
    }
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"HEAT", power:"ON", temperature:[celsius:initialsetpointtemp], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def heatCommand(){
	log.debug "Executing 'sendCommand.heatCommand'"
    def initialsetpointtemp
    def supportedfanspeed
    if (state.SupportsHeatAutoFanSpeed == "true"){
    	supportedfanspeed = "AUTO"
        } else {
        supportedfanspeed = "HIGH"
        }  
    if(device.currentValue("thermostatSetpoint") == 0){
    	initialsetpointtemp = 21
    } else {
    	initialsetpointtemp = device.currentValue("thermostatSetpoint")
    }
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"HEAT", power:"ON", temperature:[celsius:initialsetpointtemp], type:"AIR_CONDITIONING"], termination:[type:"TADO_MODE"]])
	sendCommand("temperature",[jsonbody])
}

def emergencyHeat(){
	log.debug "Executing 'sendCommand.heatCommand'"
    def initialsetpointtemp
    def supportedfanspeed
    if (state.SupportsHeatAutoFanSpeed == "true"){
    	supportedfanspeed = "AUTO"
        } else {
        supportedfanspeed = "HIGH"
        }  
    if(device.currentValue("thermostatSetpoint") == 0){
    	initialsetpointtemp = 23
    } else {
    	initialsetpointtemp = device.currentValue("thermostatSetpoint")
    }
    def jsonbody = new groovy.json.JsonOutput().toJson([setting:[fanSpeed:supportedfanspeed, mode:"HEAT", power:"ON", temperature:[celsius:initialsetpointtemp], type:"AIR_CONDITIONING"], termination:[durationInSeconds:"3600", type:"TIMER"]])
	sendCommand("temperature",[jsonbody])
}

def statusCommand(){
	log.debug "Executing 'sendCommand.statusCommand'"
	sendCommand("status",[])
}

def weatherStatusCommand(){
	log.debug "Executing 'sendCommand.weatherStatusCommand'"
	sendCommand("weatherStatus",[])
}