# multithreading Onderzoek

**1. Inleiding**

Ten behoeve van HBO ICT - Software Engineering S3 is onderzoek verricht naar Multithreading, waarvan de resultaten worden besproken in dit document.
Deze resultaten worden vervolgens gedemonstreerd middels een 'Proof of Concept' applicatie in deze Github repository.


**2. Onderzoeksvraag**

De volgende onderzoeksvragen worden middels dit onderzoek beantwoord:

* *Wat is multithreading?*
Deze vraag is de hoofdvraag en het uitgangspunt van het onderzoek. 
* *Wanneer worden meerdere threads gebruikt?*
* *Hoe wordt het onderdeel genoemd waar objecten in het geheugen worden geplaatst, en hoe is dit verschillend in een multithreaded application?*
* *Hoe wordt het onderdeel genoemd waar methoden worden uitgevoerd en primitive types in het geheugen worden geplaatst 
en hoe is dit verschillend in een multithreaded application?*
* *Wat is in dit kader een racing condition en hoe zou dit voorkomen kunnen worden?*
* *Wat is in dit kader een deadlock en hoe zou dit voorkomen kunnen worden?*


**3.1. Wat is multithreading?**

Multithreading is het gelijktijdig uitvoeren van meerdere threads. Een thread in deze context is een lichtgewicht sub-proces, met een eigen flow of execution.
Elke thread beschikt over eigen geheugenregisters voor bewerkingsvariabelen, en een eigen stack voor de procesinstructies.
Bij juiste implementatie wordt middels multithreading gebruik van een CPU core geoptimaliseerd, en is het mogelijk om instructies te parallelliseren.

**3.2. Wanneer worden meerdere threads gebruikt?**

Multithreading kan worden gebruikt voor CPU usage optimalisatie waardoor het mogelijk is dat bewerkingen sneller worden uitgevoerd. 
Een andere toepassing van multithreading is het bevorderen van de responsiveness van een applicatie, door bijvoorbeeld de weergave van de user-interface en user input handling op hun eigen thread te laten draaien zodat de gebruiker niet hoeft te wachten tot het systeem weer reageerd terwijl er interne instructies worden afgehandeld, of als er remote data wordt opgehaald.
Voor web-applicaties kan multithreading een oplossing zijn om veelvuldige of langdurige client requests af te handelen. Elke request kan parallel worden behandeld, zodat een client niet hoeft te wachten tot eerdere requests zijn behandeld.
Multithreading is niet aan te raden bij applicaties waarbij responstijden van UI of operaties niet belangrijk of insignificant zijn. De programmatuur wordt vele malen complexer, en het managen van een meerdere threads brengt wat overhead met zich mee, waardoor mogelijk juist mindere performance wordt gehaald.

**3.3. Hoe wordt het onderdeel genoemd waar objecten in het geheugen worden geplaatst, en hoe is dit verschillend in een multithreaded application?**

Objecten in het geheugen leven in de heap. De heap wordt gedeeld door threads; deze objecten kunnen dus door alle actieve threads gebruikt worden.
In een single thread applicatie is dit geen enkel probleem: er kan immers slechts één object tegelijkertijd gebruikt worden aangezien de ene thread sequentieel verloopt.

In een multithreaded applicatie kan een object door meerdere threads gebruikt worden. Het voordeel van deze gedeelde objecten is dat er parallel gewerkt kan worden met de objecten, waardoor een hogere mate van efficiëntie kan worden behaald.
Een nadeel is dat een thread de status van een object kan veranderen voordat een andere thread ermee aan de slag gaat, waardoor de uitkomst van operaties onverwacht kan zijn. Een ander nadeel is dat meerdere threads met elkaar kunnen concurreren op een object en hier tegelijktijdig op opereren, waardoor datacorruptie kan ontstaan, en het resultaat van de operatie onbetrouwbaar wordt. Dit wordt later uitgelegd in 3.5. Race condition. Deze zaken maken het programmeren van de applicatie complexer.

**3.4. Hoe wordt het onderdeel genoemd waar methoden worden uitgevoerd en primitive types in het geheugen worden geplaatst 
en hoe is dit verschillend in een multithreaded application?**

Methoden worden uitgevoerd en primitieve types worden geplaatst in de Stack. Op het moment dat er een nieuwe thread wordt gecreëerd, wordt er ook een stack hiervoor aangemaakt. Vervolgens wordt op het moment dat een nieuwe methode wordt aangeroepen in de stack een frame aangemaakt en bovenin de stack geplaatst (de JVM pusht de frame op de stack). In de frame staan lokale variabelen, en referentie variabelen naar objecten waarop geopereerd kan worden (enkel een referentie variabele naar een geheugenadres in de heap; de objecten zelf leven in de heap).
Zodra een frame voltooid is wordt deze van de stack gehaald (de JVM popt het frame), en vervolgen de operaties op het onderliggende frame. Zo onstaat er per stack een sequentiele flow of execution waarbij de methoden worden uitgevoerd volgende een Last In First Out (LIFO) principe.

In een multithreaded applicatie onstaan er dus meerdere stacks: volgens de Oracle JVM specification wordt er een nieuwe stack gecreëerd op het moment dat er een nieuwe thread wordt gecreëerd. Elke stack heeft eigen frames en lokale variabelen, wat betekend dat deze dus niet uitwisselbaar zijn. Het gedeelde geheugen waar alle threads op kunnen opereren leven in de heap, waar elke stack middels de referentievariabele naartoe refereert.

**3.5. Wat is in dit kader een racing condition en hoe zou dit voorkomen kunnen worden?**

Een racing condition is als concurrerende threads gelijktijdig proberen te opereren op een object uit de heap, waardoor de data in dit object gecorrumpeerd kan worden.
Een voorbeeld hiervan kan met een simpele loop (een "Check and Act" race condition):

for (int i = 0; 1 < 100; i++) {
  x += i;
  }

* Thread 1 checkt de conditie
* Thread 2 checkt de conditie
* Thread 1 kopieert x
* Thread 2 kopieert x
* Thread 1 opereert op x
* Thread 2 opereert op x
* Thread 1 kopieert operant terug naar x
* Thread 2 kopieert operant terug naar x
* Thread 1 verhoogd i met 1
* Thread 2 verhoogd i met 1

In bovengenoemd voorbeeld kopiëren thread 1 en 2 dezelfde waarde x; er wordt immers verwezen naar hetzelfde object, en de nieuwe waarde is nog niet terug gekopieerd. Na het opereren en terug kopiëren tellen beide echter wel i op; de waarde is dus niet meer betrouwbaar en er is een Race condition.

Dit kan worden opgelost door de methode te synchroniseren: er kan dan slechts één thread mee aan de slag; de thread heeft een "lock" op de methode.
Ook bieden meerdere programmeertalen de mogelijkheid om Atomic variabelen te gebruiken; de variabele zelf is dan door slechts één thread tegelijk te gebruiken. Er wordt een lock op de variabele geplaatst, en deze valt vrij als een thread klaar is met het opereren hierop.
Een andere oplossing is om de threads niet met elkaar te laten concurreren. Elke thread krijgt een eigen object om op te opereren. In bovenstaand geval zou de range van i verdeeld worden over de threads, en als alle threads klaar zijn de waarde van x bij elkaar worden opgeteld. Deze oplossing levert het minste overhead op, maar kan complex zijn om te implementeren.

**3.6. Wat is in dit kader een deadlock en hoe zou dit voorkomen kunnen worden?**

Een deadlock treedt op als meerdere threads op elkaar wachten om de lock op een object op te heffen. Een voorbeeld van een circulaire deadlock kunnen we omschrijven middels Edger Dijkstra's Dining Philosophers Problem:

Het probleem omschrijft dat een vijftal filosofen om een ronde tafel zit met in het midden een groot bord spaghetti. Een filosoof kan denken of eten; als deze wil eten heeft deze twee vorken nodig. Tussen elke filosoof ligt een vork, en de filosofen kunnen niet communiceren. Als een filosoof wil eten volgt hij deze procedure:

* Probeer de linker vork op te pakken
* Probeer de rechter vork op te pakken
* Eet
* Leg de linker vork neer
* Leg de rechter vork neer

Een visuele weergave van dit probleem:

![Image of DFP](https://upload.wikimedia.org/wikipedia/commons/7/7b/An_illustration_of_the_dining_philosophers_problem.png)
*Benjamin D. Esham / Wikimedia Commons*

In het kader van een multithread applicatie is elke filosoof vertegenwoordigd door een thread, en elke vork is een object op de heap. Zodra een filosoof een vork oppakt is dit object gelocked.
Problemen ontstaan als elke filosoof een linker vork heeft opgepakt: ze zullen ieder blijven wachten tot de rechter vork beschikbaar wordt. Dit zal echter nooit gebeuren gezien geen filosoof tot de instructie komt om een vork neer te leggen. We spreken van een circulaire deadlock.

Bij voorkeur willen we dit soort deadlocks voorkomen door objecten zo min mogelijk te locken. Een oplossing zou dan ook zijn om in dit geval geen shared resources toe te passen, maar elke filosoof van twee vorken voorzien.
Een andere oplossing is om een intermediair mutual exclusion lock te introduceren die kan communiceren met de filosofen. Als een filosoof een vork wil vraagt deze dat aan de intermediair, wie pas toestemming geeft als er twee vorken beschikbaar zijn, en pas doorgaat naar een andere filosoof als beide vorken zijn opgepakt.
Een nadeel van deze oplossing is dat het efficiëntie verminderd door meer overhead, en dat threads mogelijk langer moeten wachten tot de de mutex lock is opgeheven, wat kan leiden tot een discrapantie in workload. Om dit op te lossen kan een thread voorrang krijgen op de mutex unlock door deze een prioriteit toe te wijzen aan de hand van de workload, of in dit geval hoe lang geleden het is geweest dat een filosoof heeft gegeten.

**4. Proof of concept**

In de proof of concept wordt multithreading gedemonstreerd, een race condition gedemonstreerd en een oplossing geïmplementeerd voor voorgenoemde race condition.
De applicatie lost een simpel probleem op: vind de som van de getallen deelbaar door 3 en 5 in een bereik van getallen.
Om dit te bewerkstelligen wordt de range meegegeven aan een calculator object, welke middels een for loop over de range itereert, de deelbare getallen optelt en de som retourneert.
De calculator wordt als controle uitgevoerd op een enkele thread, zonder synchronisatie op twee threads om de deadlock aan te tonen en gesynchroniseerd op twee threads als oplossing. Een andere oplossing wordt gedemonstreerd door elke thread een aparte calculator mee te geven, elk over een eigen range te laten itereren en het resultaat bij elkaar op te tellen. De operaties worden geklokt om de efficiëntie van elke methodiek te testen.

**Bronnen**

* https://www.javatpoint.com/multithreading-in-java
* https://en.wikipedia.org/wiki/Multithreading_(computer_architecture)
* https://www.tutorialspoint.com/the-benefits-of-multithreaded-programming
* https://www.tutorialspoint.com/operating_system/os_multi_threading.htm
* https://blog.usejournal.com/java-multithreading-part-1-ec0c42bbead6
* https://blog.usejournal.com/java-multithreading-part-1-ec0c42bbead6
* https://alvinalexander.com/scala/fp-book/recursion-jvm-stacks-stack-frames/
* https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html
* https://javarevisited.blogspot.com/2012/02/what-is-race-condition-in.html
* https://www.geeksforgeeks.org/atomic-variables-in-java-with-examples/
* https://www.guru99.com/multithreading-java.html
* https://www.geeksforgeeks.org/dining-philosopher-problem-using-semaphores/

*Afbeelding Philosophers Dining Problem door Benjamin D. Esham ongewijzigd gebruikt onder Creative Commons licentie:* https://creativecommons.org/licenses/by-sa/3.0/deed.en
