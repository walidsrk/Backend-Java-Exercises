# RESTful JAX-RS Application : Gestion sécurisée (JWT) de données de films

## RESTful API : opérations disponibles

### Opérations associées à la gestion des utilisateurs et l'authentification

<br>
<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Authentification"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>auths/login</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Vérifier les « credentials » d’un User et renvoyer le User et un token JWT s’ils sont OK
    </td>
</tr>
<tr>
    <td>auths/register</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    Créer une ressource User et un token JWT et les renvoyer
    </td>
</tr>

</table>

<br>

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "User"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>users/init</td>
    <td>POST</td>
    <td>Non</td>
    <td>
    CREATE ONE : Créer une ressource basée sur des données par défaut (login = "james",password= "password")
    </td>
</tr>
<tr>
    <td>users/me</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ONE : Lire la ressource identifiée par le biais du token donné dans le header de la requête
    </td>
</tr>

</table>

### Opérations associées à la gestion des films

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Film"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>

<tr>
    <td>films</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ALL : Lire toutes les ressources de la collection
    </td>
</tr>
<tr>
    <td>films?minimimumDuration=value</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ALL FILTERED : Lire toutes les ressources de la collection selon le filtre donné
    </td>
</tr>
<tr>
    <td>films/{id}</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ONE : Lire la ressource identifiée
    </td>
</tr>

<tr>
    <td>films</td>
    <td>POST</td>
    <td>JWT</td>
    <td>
    CREATE ONE : Créer une ressource basée sur les données de la requête
    </td>
</tr>

<tr>
    <td>films/{id}</td>
    <td>DELETE</td>
    <td>JWT</td>
    <td>
    DELETE ONE : Effacer la ressource identifiée
    </td>
</tr>

<tr>
    <td>films/{id}</td>
    <td>PUT</td>
    <td>JWT</td>
    <td>
    UPDATE ONE : Replacer l'entièreté de la ressource par les données de la requête
    </td>
</tr>

### Opérations associées à la gestion des pages

<table style="caption-side: top">
<caption>Opérations sur les ressources de type "Page"</caption>
<tr>
    <th>URI</th>
    <th>Méthode</th>
    <th>Auths?</th>
    <th>Opération</th>
</tr>
<tr>
    <td>pages/{id}</td>
    <td>GET</td>
    <td>JWT</td>
    <td>
    READ ONE : Lire la ressource identifiée
    </td>
</tr>

<tr>
    <td>pages</td>
    <td>POST</td>
    <td>JWT</td>
    <td>
    CREATE ONE : Créer une ressource basée sur les données de la requête
    </td>
</tr>

<tr>
    <td>pages/{id}</td>
    <td>DELETE</td>
    <td>JWT</td>
    <td>
    DELETE ONE : Effacer la ressource identifiée
    </td>
</tr>

<tr>
    <td>pages/{id}</td>
    <td>PUT</td>
    <td>JWT</td>
    <td>
    UPDATE ONE : Replacer l'entièreté de la ressource par les données de la requête
    </td>
</tr>

</table>