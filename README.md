# Android_Sound_Recorder
Composant qui permet d'enregistrer un fichier audio.

<strong>Description : </strong>

Ce composant permet d'enregistrer les sons perçus par le micro dans un fichier, puis de transmettre le chemin du fichier via UPnP.

On peut imaginer que ce composant soit utilisé par un professeur qui veut permettre à ses élèves de suivre le cours à distance,
en enregistrant son cours puis le chemin du fichier audio serait transmis au composant File_Sender, ce dernier envoyant le fichier
aux élèves.

Une fois enregistré, le chemin du fichier audio est transmis par événement UPnP dans un message XML.

Voici une image de l'application une fois éxécutée :

![alt tag](https://github.com/components-upnp/Android_Sound_Recorder/blob/master/CaptureSoundRecorder.png)

L'interface est composée d'un champ pour renseigner le nom du fichier enregistré et de deux boutons, l'un pour débuter
l'enregistrement, l'autre pour l'arrêter.

<strong>Lancement de l'application : </strong>

L'application ne peut pas communiquer via UPnP lorsque lancée dans un émulateur, elle doit être lancée sur un terminal physique 
et appartenir au même réseau local que les autres composants.

Il faut donc installer l'apk sur le terminal, vérifier d'avoir autorisé les sources non vérifiées.

Après démarrage de l'application, il est possible d'ajouter le composant sur wcomp en suivant la méthode décrite sur le wiki 
oppocampus.

<strong>Spécifications UPnP : </strong>

Ce composant présente un service UPnP dont voici la description :

  SendPathService :
  
    1) SendPath(String Path) : action UPnP servant à envoyer un message XML contenant le chemin du fichier audio enregistré.
    Le message XML est transmis par un événement UPnP dont le nom est "Path".
    
Voici le schéma correspondant à ce composant :

![alt tag](https://github.com/components-upnp/Android_Sound_Recorder/blob/master/Android_Sound_Recorder.png)

<strong>Maitenance : </strong>

Le projet de l'application est un projet gradle.
