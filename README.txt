Projet d'architecture distribuée - Création d'un outil de chat en 3 tiers

Groupe : Kévin Coissard, Marion Craipeau, Elodie Goy et Théo Renouf

Notre projet comporte 4 dossiers :
- ClientCorba
- ClientWebService
- Messagerie
- Modele

ClientCorba et ClientWebService contiennent chacun un fichier du 1er tier (interface utilisateur) et des classes nécessaires à leur fonctionnement.
ClientCorba, comme son nom l'indique, devrait interragir avec le Tier2, qui se trouve dans le dossier Messagerie, grâce à Corba.
ClientWebService, devrait interragir avec le Tier2 grace à un service web.
Le tier 2 lui, accède à l'implémentation du tier3 (tier3Impl) en passant par son interface (Tier3) grâce à RMI.
L'interface est dans le dossier Messagerie, mais l'implémentation se trouve dans le dossier Modèle, avec les classes des différents objets de l'application.

Ce qui fonctionne :
Le clientWebService interragit bien grâce à un service Web avec le tier2
Le tier3 réalise la persistence des données en créant des fichiers xml.

Fonctionnalités réalisées : 
- Un utilisateur peut s'inscrire et se connecter sur la plateforme de messagerie.
- Un utilisateur peut voir la liste :
	- des personnes inscrites auquels il n'est pas connecté (aucune connexion n'est créée avec ces personnes, quelque soit l'état).
	- de ses amis
- Un utilisateur peut écrire à un ami.
- Un utilisateur voit la liste des 10 derniers utilisateurs inscrits.

Ce qui ne fonctionne pas :
- Le clientCorba
- Le tier2 n'accède pas au Tier3 pas. (removeFirst???)

N'arrivant pas à faire fonctionner la communication à distance entre le tier2 et le tier3, et n'ayant plus le temps, nous n'avons pas pu mettre en place la gestion des threads.
