
<IATA_AIDX_FlightLegNotifRQ
 xmlns:xs="http://www.w3.org/2001/XMLSchema"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
 xmlns="http://www.iata.org/IATA/2007/00" 
 AltLangID="en-us" PrimaryLangID="en-us" 
 Target="Test" Version="16.1"
 TimeStamp="${now}">
	<Originator CompanyShortName="${originator}"/>
	<DeliveringSystem CompanyShortName="${delSystem}"/>
    <FlightLeg>
    
<#include "../basis/legid.ftl" parse=true>
		<SpecialAction>Delete</SpecialAction>
		<LegData/>
	</FlightLeg>
</IATA_AIDX_FlightLegNotifRQ>