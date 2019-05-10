<?php
  if($_SERVER['REQUEST_METHOD']=='POST')
    {
      $username = $_POST['username'];
      $password = $_POST['password'];

      
      require_once('dbConnect.php');
      $sql = "SELECT firstname, lastname  FROM profiles WHERE username='$username' AND password='$password'";
      $check = mysqli_fetch_array(mysqli_query($con,$sql));
      if(isset($check))
        {
        echo 'success'." ".$check[0]." ".$check[1];
        }
      else
           {
             echo 'Invalid Username or Password';
           }
      mysqli_close($con);
    }
?>