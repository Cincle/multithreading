# multithreading Onderzoek

**1. Inleiding**

Ten behoeve van HBO ICT - Software Engineering S3 is onderzoek verricht naar Multithreading.
De resultaten van dit onderzoek zijn vervolgens gedemonstreerd middels een 'Proof of Concept' applicatie.


**2. Onderzoeksvraag**

De volgende onderzoeksvragen worden middels dit onderzoek beantwoord:

* *Wat is multithreading?*
Deze vraag is de hoofdvraag en het uitgangspunt van het onderzoek. 
* *Wanneer worden meerdere threads gebruikt?*
* *Hoe wordt het onderdeel genoemd waar objecten in het geheugen worden geplaatst, en hoe is dit verschillend in een multithreaded application?*
* *Hoe wordt het onderdeel genoemd waar methoden worden uitgevoerd en primitive types in het geheugen worden geplaatst 
en hoe is dit verschillend in een multithreaded application?*
* *Wat is in dit kader een racing condition en hoe zou dit voorkomen kunnen worden?*


**3.1. Wat is multithreading?**

Multithreading is het gelijktijdig uitvoeren van meerdere threads. Een thread in deze context is een lichtgewicht sub-proces, met een eigen flow of execution.
Elke thread beschikt over eigen geheugenregisters voor bewerkingsvariabelen, en een eigen stack voor de procesinstructies.
Bij juiste implementatie wordt middels multithreading gebruik van een CPU core geoptimaliseerd, en is het mogelijk om instructies te paralleliseren.

**3.2. Wanneer worden meerdere threads gebruikt?**

Multithreading kan worden gebruikt voor CPU usage optimalisatie waardoor het mogelijk is dat bewerkingen sneller worden uitgevoerd. 
Een andere toepassing van multithreading is het bevorderen van de responsiveness van een applicatie, door bijvoorbeeld de weergave van de user-interface en user input handling op hun eigen thread te laten draaien zodat de gebruiker niet hoeft te wachten tot het systeem weer reageerd terwijl er interne instrcties worden afgehandeld, of als er remote data wordt opgehaald.
Voor web-applicaties kan multithreading een oplossing zijn om veelvuldige of langdurige client requests af te handelen. Elke request kan parallel worden behandled, zodat een client niet hoeft te wachten tot eerdere requests zijn behandeld.
Multithreading is niet aan te raden bij applicaties waarbij responstijden van UI of operaties niet belangrijk of insignifikant zijn. De programmatuur wordt vele malen complexer, en het managen van een meerdere threads brengt wat overhead met zich mee, waardoor mogelijk juist mindere performance wordt gehaald.

**3.3. Hoe wordt het onderdeel genoemd waar objecten in het geheugen worden geplaatst, en hoe is dit verschillend in een multithreaded application?**

Objecten in het geheugen leven in de heap. De heap wordt gedeeld door threads; deze objecten kunnen dus door alle actieve threads gebruikt worden.
In een single thread applicatie is dit geen enkel probleem: er kan immers slechts één object tegelijkertijd gebruikt worden aangezien de ene thread sequentieel verloopt.
In een multithreaded applicatie kan een object door meerdere threads gebruikt worden. Het voordeel van deze gedeelde objecten is dat er parralel gewerkt kan worden met de objecten, waardoor een hogere mate van efficiëntie kan worden behaald.
Een nadeel is dat een thread de status van een object kan veranderen voordat een andere thread ermee aan de slag gaat, waardoor de uitkomst van operaties onverwacht kan zijn. Een ander nadeel is dat meerdere threads met elkaar kunnen concurreren op een object en hier tegelijktijdig op opereren, waardoor datacorruptie kan ontstaan, en het resultaat van de operatie onbetrouwbaar wordt. Dit wordt later uitgelegd in 3.5. Race condition. Deze zaken maken het programmeren van de applicatie complexer.

**3.4.
