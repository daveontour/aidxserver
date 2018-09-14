<IATA_AIDX_FlightLegNotifRQ 
     xmlns="http://www.iata.org/IATA/2007/00"  
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
     AltLangID="en-us" PrimaryLangID="en-us" 
     Version="16.1" TimeStamp="${now}">
	
	<Originator CompanyShortName="${originator}"/>
	<DeliveringSystem CompanyShortName="${delSystem}"/>
	<FlightLeg>
<#include "../basis/legid.ftl" parse=true>
		<LegData>
			<OperationalStatus RepeatIndex="1" CodeContext="9750">OFB</OperationalStatus>
			<OperationalStatus RepeatIndex="2" CodeContext="9750">ONB</OperationalStatus>
			<ServiceType>${serviceType}</ServiceType>
<#include "../basis/owner.ftl" parse=true>
			<PlannedArrivalAptHistory CodeContext="3" RepeatIndex="1">GLA</PlannedArrivalAptHistory>
<#if linkAirline??> <#include "../basis/assocFlightLeg1.ftl" parse=true> </#if>
<#if linkAirline2??> <#include "../basis/assocFlightLeg2.ftl" parse=true> </#if>
			<AirportResources Usage="Actual">
				<Resource DepartureOrArrival="Departure">
					<Runway/>
					<AircraftTerminal>5</AircraftTerminal>
					<PublicTerminal RepeatIndex="1">5</PublicTerminal>
				</Resource>
				<Resource DepartureOrArrival="Arrival">
					<Runway/>
					<AircraftTerminal>M</AircraftTerminal>
					<PublicTerminal RepeatIndex="1">M</PublicTerminal>
				</Resource>
			</AirportResources>
			<OperationTime CodeContext="9750" RepeatIndex="1" TimeType="TAR" OperationQualifier="SRT" xsi:nil="true"/>
			<OperationTime CodeContext="9750" RepeatIndex="2" TimeType="SCT" OperationQualifier="OFB">${sctofb}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="3" TimeType="ACT" OperationQualifier="OFB">${actofb}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="4" TimeType="CAL" OperationQualifier="TKO">${caltko}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="5" TimeType="TAR" OperationQualifier="TKO" xsi:nil="true"/>
			<OperationTime CodeContext="9750" RepeatIndex="6" TimeType="ACT" OperationQualifier="TKO">${acttko}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="7" TimeType="ACT" OperationQualifier="TDN">${acttdn}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="8" TimeType="SCT" OperationQualifier="ONB">${sctonb}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="9" TimeType="ACT" OperationQualifier="ONB">${actonb}</OperationTime>
<#include "../basis/acInfo.ftl" parse=true>
		</LegData>
	</FlightLeg>
</IATA_AIDX_FlightLegNotifRQ>