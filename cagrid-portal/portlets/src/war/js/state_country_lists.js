
var CGP_StateCountryLists = Class.create({

	initialize: function(stateFieldId, countryFieldId, selectedState, selectedCountry){
		
		this.stateFieldId = stateFieldId;
		this.countryFieldId = countryFieldId;
		
		this.defaultCountry = "US";
		this.selectedState = selectedState;
		this.selectedCountry = selectedCountry;
	},
	
	render: function(){
		var thisObj = this;
		var elId = "#" + this.countryFieldId;
		jQuery(elId).html(this.countryOptions);
		jQuery(elId).bind("change", function(){
			thisObj.update(jQuery(elId + " option:selected").val());
		});	
		
		if(this.selectedCountry){
			this.update(this.selectedCountry);
			jQuery("#" + this.countryFieldId + " > option[value='" + this.selectedCountry + "']").attr("selected", "true");
			if(this.selectedState){
				jQuery("#" + this.stateFieldId + " > option[value='" + this.selectedState + "']").attr("selected", "true");
			}
		}else{
			this.update(this.defaultCountry);
		}
	},
	
	update: function(country){
		var stateOptions = null;
		if(country == "US"){
			stateOptions = this.stateOptionsUS;
		}else if(country == "CA"){
			stateOptions = this.stateOptionsCA
		}else{
			stateOptions = this.stateOptionsOther;
		}
		jQuery("#" + this.stateFieldId).html(stateOptions);
	},

	stateOptionsUS:
		'<option value="AK" selected>AK-Alaska</option>' +
		'<option value="AL">AL-Alabama</option>' +
		'<option value="AR">AR-Arkansas</option>' +
		'<option value="AZ">AZ-Arizona</option>' +
		'<option value="CA">CA-California</option>' +
		'<option value="CO">CO-Colorado</option>' +
		'<option value="CT">CT-Connecticut</option>' +
		'<option value="DC">DC-District of Columbia</option>' +
		'<option value="DE">DE-Delaware</option>' +
		'<option value="FL">FL-Florida</option>' +
		'<option value="GA">GA-Georgia</option>' +
		'<option value="HI">HI-Hawaii</option>' +
		'<option value="IA">IA-Iowa</option>' +
		'<option value="ID">ID-Idaho</option>' +
		'<option value="IL">IL-Illinois</option>' +
		'<option value="IN">IN-Indiana</option>' +
		'<option value="KS">KS-Kansas</option>' +
		'<option value="KY">KY-Kentucky</option>' +
		'<option value="LA">LA-Louisiana</option>' +
		'<option value="MA">MA-Massachusetts</option>' +
		'<option value="MD">MD-Maryland</option>' +
		'<option value="ME">ME-Maine</option>' +
		'<option value="MI">MI-Michigan</option>' +
		'<option value="MN">MN-Minnesota</option>' +
		'<option value="MO">MO-Missouri</option>' +
		'<option value="MS">MS-Mississippi</option>' +
		'<option value="MT">MT-Montana</option>' +
		'<option value="NC">NC-North Carolina</option>' +
		'<option value="ND">ND-North Dakota</option>' +
		'<option value="NE">NE-Nebraska</option>' +
		'<option value="NH">NH-New Hampshire</option>' +
		'<option value="NJ">NJ-New Jersey</option>' +
		'<option value="NM">NM-New Mexico</option>' +
		'<option value="NV">NV-Nevada</option>' +
		'<option value="NY">NY-New York</option>' +
		'<option value="OH">OH-Ohio</option>' +
		'<option value="OK">OK-Oklahoma</option>' +
		'<option value="OR">OR-Oregon</option>' +
		'<option value="PA">PA-Pennsylvania</option>' +
		'<option value="RI">RI-Rhode Island</option>' +
		'<option value="SC">SC-South Carolina</option>' +
		'<option value="SD">SD-South Dakota</option>' +
		'<option value="TN">TN-Tennessee</option>' +
		'<option value="TX">TX-Texas</option>' +
		'<option value="UT">UT-Utah</option>' +
		'<option value="VA">VA-Virginia</option>' +
		'<option value="VT">VT-Vermont</option>' +
		'<option value="WA">WA-Washington</option>' +
		'<option value="WI">WI-Wisconsin</option>' +
		'<option value="WV">WV-West Virginia</option>' +
		'<option value="WY">WY-Wyoming</option>',
		
	stateOptionsCA:
		'<option value="AB" selected>AB-Alberta</option>' +
		'<option value="BC">BC-British Columbia</option>' +
		'<option value="MB">MB-Manitoba</option>' +
		'<option value="NB">NB-New Brunswick</option>' +
		'<option value="NL">NL-Newfoundland and Labrador</option>' +
		'<option value="NT">NT-Northwest Territories</option>' +
		'<option value="NS">NS-Nova Scotia</option>' +
		'<option value="NU">NU-Nunavut</option>' +
		'<option value="ON">ON-Ontario</option>' +
		'<option value="PE">PE-Prince Edward Island</option>' +
		'<option value="QC">QC-Quebec</option>' +
		'<option value="SK">SK-Saskatchewan</option>' +
		'<option value="YT">YT-Yukon</option>',
		
	stateOptionsOther:
		'<option value="Outside_US" selected>Other</option>',
		
	countryOptions:
	     '<option value="US" selected>United States of America</option>' +
	     '<option value="CA">Canada</option>' +
	     '<option value="AF">Afghanistan</option>' +
	     '<option value="AX">Aland Islands</option>' +
	     '<option value="AL">Albania</option>' +
	     '<option value="DZ">Algeria</option>' +
	     '<option value="AS">American Samoa</option>' +
	     '<option value="AD">Andorra</option>' +
	     '<option value="AO">Angola</option>' +
	     '<option value="AI">Anguilla</option>' +
	     '<option value="AQ">Antarctica</option>' +
	     '<option value="AG">Antigua and Barbuda</option>' +
	     '<option value="AR">Argentina</option>' +
	     '<option value="AM">Armenia</option>' +
	     '<option value="AW">Aruba</option>' +
	     '<option value="AU">Australia</option>' +
	     '<option value="AT">Austria</option>' +
	     '<option value="AZ">Azerbaijan</option>' +
	     '<option value="BS">Bahamas</option>' +
	     '<option value="BH">Bahrain</option>' +
	     '<option value="BD">Bangladesh</option>' +
	     '<option value="BB">Barbados</option>' +
	     '<option value="BY">Belarus</option>' +
	     '<option value="BE">Belgium</option>' +
	     '<option value="BZ">Belize</option>' +
	     '<option value="BJ">Benin</option>' +
	     '<option value="BM">Bermuda</option>' +
	     '<option value="BT">Bhutan</option>' +
	     '<option value="BO">Bolivia</option>' +
	     '<option value="BA">Bosnia and Herzegovina</option>' +
	     '<option value="BW">Botswana</option>' +
	     '<option value="BV">Bouvet Island</option>' +
	     '<option value="BR">Brazil</option>' +
	     '<option value="IO">British Indian Ocean Territory</option>' +
	     '<option value="BN">Brunei Darussalam</option>' +
	     '<option value="BG">Bulgaria</option>' +
	     '<option value="BF">Burkina Faso</option>' +
	     '<option value="BI">Burundi</option>' +
	     '<option value="KH">Cambodia</option>' +
	     '<option value="CM">Cameroon</option>' +
	     '<option value="CV">Cape Verde</option>' +
	     '<option value="KY">Cayman Islands</option>' +
	     '<option value="CF">Central African Republic</option>' +
	     '<option value="TD">Chad</option>' +
	     '<option value="CL">Chile</option>' +
	     '<option value="CN">China</option>' +
	     '<option value="CX">Christmas Island</option>' +
	     '<option value="CC">Cocos (Keeling) Islands</option>' +
	     '<option value="CO">Colombia</option>' +
	     '<option value="KM">Comoros</option>' +
	     '<option value="CG">Congo (Brazzaville)</option>' +
	     '<option value="CD">Congo (Kinshasa)</option>' +
	     '<option value="CK">Cook Islands</option>' +
	     '<option value="CR">Costa Rica</option>' +
	     '<option value="CI">C�te dIvoire</option>' +
	     '<option value="HR">Croatia</option>' +
	     '<option value="CU">Cuba</option>' +
	     '<option value="CY">Cyprus</option>' +
	     '<option value="CZ">Czech Republic</option>' +
	     '<option value="DK">Denmark</option>' +
	     '<option value="DJ">Djibouti</option>' +
	     '<option value="DM">Dominica</option>' +
	     '<option value="DO">Dominican Republic</option>' +
	     '<option value="EC">Ecuador</option>' +
	     '<option value="EG">Egypt</option>' +
	     '<option value="SV">El Salvador</option>' +
	     '<option value="GQ">Equatorial Guinea</option>' +
	     '<option value="ER">Eritrea</option>' +
	     '<option value="EE">Estonia</option>' +
	     '<option value="ET">Ethiopia</option>' +
	     '<option value="FK">Falkland Islands</option>' +
	     '<option value="FO">Faroe Islands</option>' +
	     '<option value="FJ">Fiji</option>' +
	     '<option value="FI">Finland</option>' +
	     '<option value="FR">France</option>' +
	     '<option value="GF">French Guiana</option>' +
	     '<option value="PF">French Polynesia</option>' +
	     '<option value="TF">French Southern Lands</option>' +
	     '<option value="GA">Gabon</option>' +
	     '<option value="GM">Gambia</option>' +
	     '<option value="GE">Georgia</option>' +
	     '<option value="DE">Germany</option>' +
	     '<option value="GH">Ghana</option>' +
	     '<option value="GI">Gibraltar</option>' +
	     '<option value="GR">Greece</option>' +
	     '<option value="GL">Greenland</option>' +
	     '<option value="GD">Grenada</option>' +
	     '<option value="GP">Guadeloupe</option>' +
	     '<option value="GU">Guam</option>' +
	     '<option value="GT">Guatemala</option>' +
	     '<option value="GG">Guernsey</option>' +
	     '<option value="GN">Guinea</option>' +
	     '<option value="GW">Guinea-Bissau</option>' +
	     '<option value="GY">Guyana</option>' +
	     '<option value="HT">Haiti</option>' +
	     '<option value="HM">Heard and McDonald Islands</option>' +
	     '<option value="HN">Honduras</option>' +
	     '<option value="HK">Hong Kong</option>' +
	     '<option value="HU">Hungary</option>' +
	     '<option value="IS">Iceland</option>' +
	     '<option value="IN">India</option>' +
	     '<option value="ID">Indonesia</option>' +
	     '<option value="IR">Iran</option>' +
	     '<option value="IQ">Iraq</option>' +
	     '<option value="IE">Ireland</option>' +
	     '<option value="IM">Isle of Man</option>' +
	     '<option value="IL">Israel</option>' +
	     '<option value="IT">Italy</option>' +
	     '<option value="JM">Jamaica</option>' +
	     '<option value="JP">Japan</option>' +
	     '<option value="JE">Jersey</option>' +
	     '<option value="JO">Jordan</option>' +
	     '<option value="KZ">Kazakhstan</option>' +
	     '<option value="KE">Kenya</option>' +
	     '<option value="KI">Kiribati</option>' +
	     '<option value="KP">Korea ( North )</option>' +
	     '<option value="KR">Korea ( South )</option>' +
	     '<option value="KW">Kuwait</option>' +
	     '<option value="KG">Kyrgyzstan</option>' +
	     '<option value="LA">Laos</option>' +
	     '<option value="LV">Latvia</option>' +
	     '<option value="LB">Lebanon</option>' +
	     '<option value="LS">Lesotho</option>' +
	     '<option value="LR">Liberia</option>' +
	     '<option value="LY">Libya</option>' +
	     '<option value="LI">Liechtenstein</option>' +
	     '<option value="LT">Lithuania</option>' +
	     '<option value="LU">Luxembourg</option>' +
	     '<option value="MO">Macau</option>' +
	     '<option value="MK">Macedonia</option>' +
	     '<option value="MG">Madagascar</option>' +
	     '<option value="MW">Malawi</option>' +
	     '<option value="MY">Malaysia</option>' +
	     '<option value="MV">Maldives</option>' +
	     '<option value="ML">Mali</option>' +
	     '<option value="MT">Malta</option>' +
	     '<option value="MH">Marshall Islands</option>' +
	     '<option value="MQ">Martinique</option>' +
	     '<option value="MR">Mauritania</option>' +
	     '<option value="MU">Mauritius</option>' +
	     '<option value="YT">Mayotte</option>' +
	     '<option value="MX">Mexico</option>' +
	     '<option value="FM">Micronesia</option>' +
	     '<option value="MD">Moldova</option>' +
	     '<option value="MC">Monaco</option>' +
	     '<option value="MN">Mongolia</option>' +
	     '<option value="ME">Montenegro</option>' +
	     '<option value="MS">Montserrat</option>' +
	     '<option value="MA">Morocco</option>' +
	     '<option value="MZ">Mozambique</option>' +
	     '<option value="MM">Myanmar</option>' +
	     '<option value="NA">Namibia</option>' +
	     '<option value="NR">Nauru</option>' +
	     '<option value="NP">Nepal</option>' +
	     '<option value="NL">Netherlands</option>' +
	     '<option value="AN">Netherlands Antilles</option>' +
	     '<option value="NC">New Caledonia</option>' +
	     '<option value="NZ">New Zealand</option>' +
	     '<option value="NI">Nicaragua</option>' +
	     '<option value="NE">Niger</option>' +
	     '<option value="NG">Nigeria</option>' +
	     '<option value="NU">Niue</option>' +
	     '<option value="NF">Norfolk Island</option>' +
	     '<option value="MP">Northern Mariana Islands</option>' +
	     '<option value="NO">Norway</option>' +
	     '<option value="OM">Oman</option>' +
	     '<option value="PK">Pakistan</option>' +
	     '<option value="PW">Palau</option>' +
	     '<option value="PS">Palestine</option>' +
	     '<option value="PA">Panama</option>' +
	     '<option value="PG">Papua New Guinea</option>' +
	     '<option value="PY">Paraguay</option>' +
	     '<option value="PE">Peru</option>' +
	     '<option value="PH">Philippines</option>' +
	     '<option value="PN">Pitcairn</option>' +
	     '<option value="PL">Poland</option>' +
	     '<option value="PT">Portugal</option>' +
	     '<option value="PR">Puerto Rico</option>' +
	     '<option value="QA">Qatar</option>' +
	     '<option value="RE">Reunion</option>' +
	     '<option value="RO">Romania</option>' +
	     '<option value="RU">Russian Federation</option>' +
	     '<option value="RW">Rwanda</option>' +
	     '<option value="BL">Saint Barth�lemy</option>' +
	     '<option value="SH">Saint Helena</option>' +
	     '<option value="KN">Saint Kitts and Nevis</option>' +
	     '<option value="LC">Saint Lucia</option>' +
	     '<option value="MF">Saint Martin (French part)</option>' +
	     '<option value="PM">Saint Pierre and Miquelon</option>' +
	     '<option value="VC">Saint Vincent and the Grenadines</option>' +
	     '<option value="WS">Samoa</option>' +
	     '<option value="SM">San Marino</option>' +
	     '<option value="ST">Sao Tome and Principe</option>' +
	     '<option value="SA">Saudi Arabia</option>' +
	     '<option value="SN">Senegal</option>' +
	     '<option value="RS">Serbia</option>' +
	     '<option value="SC">Seychelles</option>' +
	     '<option value="SL">Sierra Leone</option>' +
	     '<option value="SG">Singapore</option>' +
	     '<option value="SK">Slovakia</option>' +
	     '<option value="SI">Slovenia</option>' +
	     '<option value="SB">Solomon Islands</option>' +
	     '<option value="SO">Somalia</option>' +
	     '<option value="ZA">South Africa</option>' +
	     '<option value="GS">South Georgia and South Sandwich Islands</option>' +
	     '<option value="KR">South Korea</option>' +
	     '<option value="ES">Spain</option>' +
	     '<option value="LK">Sri Lanka</option>' +
	     '<option value="SD">Sudan</option>' +
	     '<option value="SR">Suriname</option>' +
	     '<option value="SJ">Svalbard and Jan Mayen Islands</option>' +
	     '<option value="SZ">Swaziland</option>' +
	     '<option value="SE">Sweden</option>' +
	     '<option value="CH">Switzerland</option>' +
	     '<option value="SY">Syria</option>' +
	     '<option value="TW">Taiwan</option>' +
	     '<option value="TJ">Tajikistan</option>' +
	     '<option value="TZ">Tanzania</option>' +
	     '<option value="TH">Thailand</option>' +
	     '<option value="TL">Timor-Leste</option>' +
	     '<option value="TG">Togo</option>' +
	     '<option value="TK">Tokelau</option>' +
	     '<option value="TO">Tonga</option>' +
	     '<option value="TT">Trinidad and Tobago</option>' +
	     '<option value="TN">Tunisia</option>' +
	     '<option value="TR">Turkey</option>' +
	     '<option value="TM">Turkmenistan</option>' +
	     '<option value="TC">Turks and Caicos Islands</option>' +
	     '<option value="TV">Tuvalu</option>' +
	     '<option value="UG">Uganda</option>' +
	     '<option value="UA">Ukraine</option>' +
	     '<option value="AE">United Arab Emirates</option>' +
	     '<option value="GB">United Kingdom</option>' +
	     '<option value="UM">United States Minor Outlying Islands</option>' +
	     '<option value="UY">Uruguay</option>' +
	     '<option value="UZ">Uzbekistan</option>' +
	     '<option value="VU">Vanuatu</option>' +
	     '<option value="VA">Vatican City</option>' +
	     '<option value="VE">Venezuela</option>' +
	     '<option value="VN">Vietnam</option>' +
	     '<option value="VG">Virgin Islands ( British )</option>' +
	     '<option value="VI">Virgin Islands ( U.S. )</option>' +
	     '<option value="WF">Wallis and Futuna Islands</option>' +
	     '<option value="EH">Western Sahara</option>' +
	     '<option value="YE">Yemen</option>' +
	     '<option value="ZM">Zambia</option>' +
	     '<option value="ZW">Zimbabwe</option>'	

});