
<IATA_AIDX_FlightLegNotifRQ 
xmlns="http://www.iata.org/IATA/2007/00" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
AltLangID="en-us" PrimaryLangID="en-us" 
Target="Test" Version="16.1" TimeStamp="${now}">
	<Originator CompanyShortName="${originator}"/>
	<DeliveringSystem CompanyShortName="${delSystem}"/>
	<FlightLeg>
<#include "../basis/legid.ftl" parse=true>
		<LegData>
			<OperationalStatus RepeatIndex="1" CodeContext="1245">SCH</OperationalStatus>
			<OperationalStatus RepeatIndex="2" CodeContext="1245">SCH</OperationalStatus>
			<OperationalStatus RepeatIndex="3">DX</OperationalStatus>
			<ServiceType>${serviceType}</ServiceType>
<#include "../basis/owner.ftl" parse=true>
			<PlannedArrivalAptHistory CodeContext="3" RepeatIndex="1">MUC</PlannedArrivalAptHistory>
			<AssociatedFlightLegAircraft RepeatIndex="1" xsi:nil="true"/>
			<AssociatedFlightLegAircraft RepeatIndex="2" xsi:nil="true"/>
			<AirportResources Usage="Actual">
				<Resource DepartureOrArrival="Departure">
					<Runway/>
					<AircraftTerminal>5</AircraftTerminal>
					<PublicTerminal RepeatIndex="1">5</PublicTerminal>
				</Resource>
				<Resource DepartureOrArrival="Arrival">
					<Runway/>
					<AircraftTerminal>1</AircraftTerminal>
					<PublicTerminal RepeatIndex="1">1</PublicTerminal>
				</Resource>
			</AirportResources>
			<OperationTime CodeContext="9750" RepeatIndex="1" TimeType="TAR" OperationQualifier="SRT" xsi:nil="true"/>
			<OperationTime CodeContext="9750" RepeatIndex="2" TimeType="SCT" OperationQualifier="OFB">${sctofb}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="3" TimeType="CAL" OperationQualifier="TKO">${caltko}</OperationTime>
			<OperationTime CodeContext="9750" RepeatIndex="4" TimeType="TAR" OperationQualifier="TKO" xsi:nil="true"/>
			<OperationTime CodeContext="9750" RepeatIndex="5" TimeType="ACT" OperationQualifier="TKO" xsi:nil="true"/>
			<OperationTime CodeContext="9750" RepeatIndex="6" TimeType="ACT" OperationQualifier="TDN" xsi:nil="true"/>
			<OperationTime CodeContext="9750" RepeatIndex="7" TimeType="SCT" OperationQualifier="ONB">${sctonb}</OperationTime>
<#include "../basis/acInfo.ftl" parse=true>
		</LegData>
	</FlightLeg>
</IATA_AIDX_FlightLegNotifRQ>