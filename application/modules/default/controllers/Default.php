<?php

if (!defined('BASEPATH'))
	exit('No direct script access allowed');

class Default extends CI_Controller {

	public function __construct() {
		parent::__construct();
	}

	function index() {
		echo "Hello there!";
	}
}
?>