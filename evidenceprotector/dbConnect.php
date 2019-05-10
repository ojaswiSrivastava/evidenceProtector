<?php
 define('HOST','localhost');
 define('USER','root');
 define('DB','userprofiles');
 
 $con = mysqli_connect(HOST,USER,NULL,DB) or die('Unable to Connect');
