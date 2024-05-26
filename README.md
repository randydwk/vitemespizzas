# **vitemespizzas**

<img src="/assets/baniere.jpg">

## Table des matières
  * [Description](#description)
  * [Structure du projet](#structure-du-projet)
  * [Prérequis](#prerequis)
  * [Cloner le dépot](#cloner-le-depot)
  * [Installation et Compilation](#installation-et-compilation)
  * [Les endpoint](#les-endpoint)
  * [Diagrame de classes](#diagrame-de-classes)
  * [MCD](#modele-conceptuel-de-donnes)
  * [License](#license)

* * *

## Description
Ce dépot contient la production de [Randy DE WANCKER](https://gitlab.univ-lille.fr/randy.dewancker.etu) et de [Alexandre DEUDON](https://gitlab.univ-lille.fr/alexandre.deudon.etu) pour la SAE4.A02.1
et conciste en un projet java visant a implémenter une API rest de menu et commandes fictives de 
pizzas

* * *

## Structure du projet
```shell
. # racine du projet
├── assets # contenu additionel, images, pdf, ect...
├── META-INF # contient le context.xml decrivant le processus de déploiement
└── WEB-INF # contient les sources et classes des servlets 
    ├── classes ▶ # Contient la méme structure que src/ mais avec des fichiers compilés
    └── src # Contient les fichiers sources des servlets
        ├── controleurs # Classes qui traite les actions de l'utilisateur
        ├── dao # Data Access Object
        ├── dataset # Les jeux de données
        └── dto # Data Transfer Objet
```

## Prérequis  
Ce qu'il vous faut pour pouvoir utiliser ce projet
+ Le kit de développement java n°11 qui peut être installé :
    * Sous Windows grâce à cet [installateur](https://adoptium.net/en-GB/temurin/releases/?version=11)
    * Sous Linux avec la commande <code>sudo apt install open-jdk-11</code>
+ Le gestionaire de projet maven : [Télécharger maven](https://maven.apache.org/download.cgi)
+ tomcat9 : [Télécharger tomcat9](https://tomcat.apache.org/download-90.cgi)  

> :warning: ce projet ne prend pas en charge le packaging WAR et n'est donc pas compatible avec les 
> versions tomcat 10 et +


## Cloner le dépôt
À l'aide d'un terminal, naviguez jusqu'au répertoire `webapps` de votre installation de tomcat

```shell
git clone https://gitlab.univ-lille.fr/alexandre.deudon.etu/vitemespizzas.git
```


## Installation et Compilation
Après avoir cloné le dépôt ouvrez/déplacez un terminal dans le répertoire `vitemespizzas` et
executez les commandes adaptée(s) selon ce que vous souhaitez faire

> **Installer les dépendances**
> ```shell
> mvn install
> ```
> **Compiler le projet**
> ```shell
> mvn clean compile
> ```

## Les endpoints

#### Ingrédients  
  
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/ingredients
```  
> retourne la liste de tout les ingredients  
>   
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/ingredients/1
```  
> retourne les caractéristique de l'ingredient numéro 1  
>   
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/ingredients/1/name
```  
> retourne uniquement le nom de l'ingredient numéro 1  
>   
```shell
curl -i -X POST http://localhost:8080/vitemespizzas/ingredients -H "Authorization: Bearer <token>" -d '{"id":20, "name":"ingredient", "prix":0.2}'
```  
> rajoute l'ingredient numéro 20 avec un prix de 0.2€  
>   
```shell
curl -i -X DELETE http://localhost:8080/vitemespizzas/ingredients/20 -H "Authorization: Bearer  <token>"
```  
> suprimme l'ingredient numéro 20  
    
#### Pizzas  
  
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/pizzas
```  
> retourne la liste de toute les pizzas  
>   
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/pizzas/1
```  
> retourne la liste de toute les caracteristiques de la pizza numéro 1  
>   
```shell
curl -i -X POST http://localhost:8080/vitemespizzas/pizzas -H "Authorization: Bearer <token>" -d '{"id":10, "name":"4 fromages", "type":"tomate", "prixBase":7}'
```  
> ajoute la pizza 4 fromages avec l'id 10 et le prix de base de 7€  
>   
```shell
curl -i -X DELETE http://localhost:8080/vitemespizzas/pizzas/10
```  
> suprimme (à mon grand regret) la pizza d'id 10 (la 4 fromages)  
>   
```shell
curl -i -X PATCH http://localhost:8080/vitemespizzas/pizzas/1 -H "Authorization: Bearer <token>" -d '{"type":"creme"}'
```  
> modifie le type de la pizza d'id 1  
>  
```shell
curl -i -X POST http://localhost:8080/vitemespizzas/pizzas/1 -H "Authorization: Bearer <token>" -d '{"ings":"[{"id":"1"}]"}'
```  
> ajoute l'ingrédient "pomme de terre" à la pizza d'id 1  
>   
```shell
curl -i -X DELETE http://localhost:8080/vitemespizzas/pizzas/3/6
```  
> retire l'ingrédient d'id 6 (les champignons) de la pizza d'id 3 (reine)  
>   
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/pizzas/2/prixfinal 
```  
> fournit le prix final de la pizza d'id 2
  
#### Commandes  

```shell
curl -i -X GET http://localhost:8080/vitemespizzas/commandes
``` 
> fournit toutes les commandes et leurs détails
>
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/commandes/1
``` 
> fournit les détails de la commande d'id 1
>
```shell
curl -i -X POST http://localhost:8080/vitemespizzas/commandes -H "Authorization: Bearer <token>" -d '{"id":4,"user":2,"date":"2022-11-08"}'
``` 
> ajoute une commande passée par l'utilisateur d'id 2 le 8 novembre 2022
>
```shell
curl -i -X GET http://localhost:8080/vitemespizzas/commandes/2/prixfinal
``` 
> fournit le prix final de commande d'id 2

#### Utilisateurs  
  
```shell
curl -i -X GET "http://localhost:8080/vitemespizzas/users/token?login=jean&pwd=jean"
```  
> fournit le jeton d'authentification de jean  

* * *

## Modele conceptuel de donnes
qui peut être généré par le script [basePizzas.sql](/basePizzas.sql)

<img src="/assets/mcd.png">

## Diagramme de classes

<img src="/assets/class_diagram.svg">

## Licence
L'ensemble des productions sur ce dépot sont couvertes par la licence by-nc-sa 4.0.
[<img src="https://licensebuttons.net/l/by-nc-sa/3.0/88x31.png">](https://creativecommons.org/licenses/by-nc-sa/4.0/ "License")
