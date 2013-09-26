/**
 * 
 */
package com.telefonica.euro_iaas.sdc.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;


/**
 * Models a Chef Client
 * 
 * @author jesus.movilla
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChefClient {

	private String name;
	private String chefType = "node";
	private String jsonClass = "Chef::Node";
	private String publickey;
	private String _rev;
	private String admin;
	
	/**
    *
    */
	public ChefClient() {}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the chefType
	 */
	public String getChefType() {
		return chefType;
	}
	/**
	 * @param chefType the chefType to set
	 */
	public void setChefType(String chefType) {
		this.chefType = chefType;
	}
	/**
	 * @return the jsonClass
	 */
	public String getJsonClass() {
		return jsonClass;
	}
	/**
	 * @param jsonClass the jsonClass to set
	 */
	public void setJsonClass(String jsonClass) {
		this.jsonClass = jsonClass;
	}
	/**
	 * @return the publickey
	 */
	public String getPublickey() {
		return publickey;
	}
	/**
	 * @param publickey the publickey to set
	 */
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}
	/**
	 * @return the _rev
	 */
	public String get_rev() {
		return _rev;
	}
	/**
	 * @param _rev the _rev to set
	 */
	public void set_rev(String _rev) {
		this._rev = _rev;
	}
	/**
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	/**
	 * JSon serializer
	 */
	public String toJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("name", name);
		jsonObject.accumulate("json_class", "Chef::Node");
		jsonObject.accumulate("chef_type", "node");
		jsonObject.accumulate("public_key", publickey);
		jsonObject.accumulate("_rev", _rev);
		jsonObject.accumulate("admin", admin);
		return jsonObject.toString();
	}
	
	@SuppressWarnings("unchecked")
	public void fromJson(JSONObject jsonNode) {
		name = jsonNode.getString("name");
		publickey = jsonNode.getString("public_key");
		_rev = jsonNode.getString("_rev");
		admin = jsonNode.getString("admin");
	}
	
	@SuppressWarnings("unchecked")
	public void fromKnifeCommand(String output) {
		
		String[] claveValor = output.split("\n");
		//String[] claveValor = output.split(": ");
		name = getValue(claveValor,"name");
		publickey = getValue(claveValor,"public_key");
		_rev = getValue(claveValor,"_rev");
		admin = getValue(claveValor,"admin");
	}
	
	//Hay un error con publickey
	private String getValue (String[] claveValor, String clave) {
		String valor = null;
		for (int i=0; i < claveValor.length; i++){
			String clavevalor = (String)claveValor[i];
			if (clavevalor.contains(clave)) {
				valor = (clavevalor.replace(clave + ":"," ")).trim();
			}
			
		}
		return valor;
	}
	
	@SuppressWarnings("unchecked")
	public String getChefClientName(String stringChefClients, String hostname) {
		
		String[] output = stringChefClients.split("\"" + hostname );
		String url = output[1].split("\"")[2];
		String nameAux =  url.split("clients")[1];
		String name = nameAux.substring(1,nameAux.length());
		return name;
	}
}