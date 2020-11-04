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

**3.4. Hoe wordt het onderdeel genoemd waar methoden worden uitgevoerd en primitive types in het geheugen worden geplaatst 
en hoe is dit verschillend in een multithreaded application?**

Methoden worden uitgevoerd en primitieve types worden geplaatst in de Stack. Op het moment dat er een nieuwe thread wordt gecreëerd, wordt er ook een stack hiervoor aangemaakt. Vervolgens wordt op het moment dat een nieuwe methode wordt aangeroepen in de stack een frame aangemaakt en bovenin de stack geplaatst(de JVM pusht de frame op de stack). In de frame staan lokale variabelen, en referentie variabelen naar objecten waarop geopereerd kan worden (enkel een referentie variabele naar een geheugenadres in de heap; de objecten zelf leven in de heap).
Zodra een frame voltooid is wordt deze van de stack gehaald (de JVM popt het frame), en vervolgen de operaties op het onderliggende frame. Zo onstaat er per stack een sequentiele flow of execution waarbij de methoden worden uitgevoerd volgende een Last In First Out (LIFO) principe.

In een multithreaded applicatie onstaan er dus meerdere stacks: volgens de Oracle JVM specification wordt er een nieuwe stack gecreëerd op het moment dat er een nieuwe thread wordt gecreëerd. Elke stack heeft eigen frames en lokale variabelen, wat betekend dat deze dus niet uitwisselbaar zijn. Het gedeelde geheugen waar alle threads op kunnen opereren leven in de heap, waar elke stack middels de referentievariabele naartoe refereerd.

**3.5. Wat is in dit kader een racing condition en hoe zou dit voorkomen kunnen worden?**

Een racing condition is als concurrerende threads gelijktijdig proberen te opereren op een object uit de heap, waardoor de data in dit object gecorrupteerd kan worden.
Een voorbeeld hiervan kan met een simpele loop (een "Check and Act" race condition):

for (int i = 0; 1 < 100; i++) {
  x += i;
  }

* Thread 1 checkt de conditie
* Thread 2 checkt de conditie
* Thread 1 kopieerd x
* Thread 2 kopieerd x
* Thread 1 opereerd op x
* Thread 2 opereerd op x
* Thread 1 kopieerd operant terug naar x
* Thread 2 kopieerd operant terug naar x
* Thread 1 verhoogd i met 1
* Thread 2 verhoogd i met 1

In bovengenoemd voorbeeld kopieren thread 1 en 2 dezelfde waarde x; er wordt immers verwezen naar hetzelfde object, en de nieuwe waarde is nog niet terug gekopieerd. Na het opereren en terug kopieren tellen beide echter wel i op; de waarde is dus niet meer betrouwbaar en er is een Race condition.

Dit kan worden opgelost door de methode te synchroniseren: er kan dan slechts één thread mee aan de slag; de thread heeft een "lock" op de methode.
Ook bieden meerdere programmeertalen de mogelijkheid om Atomic variabelen te gebruiken; de variabele zelf is dan door slechts één thread tegelijk te gebruiken. Er wordt een lock op de variabele geplaatst, en deze valt vrij als een thread klaar is met het opereren hierop.
Een andere oplossing is om de threads niet met elkaar te laten concurreren. Elke thread krijgt een eigen object om op te opereren. In bovenstaand geval zou de range van i verdeeld worden over de threads, en als alle threads klaar zijn de waarde van x bij elkaar worden opgeteld. Deze oplossing leverd het minst overhead op, maar kan complex zijn om te implementeren.
