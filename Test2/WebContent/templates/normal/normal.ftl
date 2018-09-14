<IATA_AIDX_FlightLegNotifRQ 
	xmlns="http://www.iata.org/IATA/2007/00" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	AltLangID="en-us" PrimaryLangID="en-us" 
	Target="Test" Version="16.1" 
	TimeStamp="${now}" >
	
	<Originator CompanyShortName="${originator}"/>
	<DeliveringSystem CompanyShortName="${delSystem}"/>
    <FlightLeg>
    
<#include "../basis/legid.ftl" parse=true>

		<LegData>
			<OperationalStatus RepeatIndex="1" CodeContext="1245">SCH</OperationalStatus>
			<OperationalStatus RepeatIndex="2" CodeContext="1245">SCH</OperationalStatus>
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
<#include "../basis/optimes.ftl" parse=true>
<#include "../basis/acInfo.ftl" parse=true>
		</LegData>
	</FlightLeg>
</IATA_AIDX_FlightLegNotifRQ>