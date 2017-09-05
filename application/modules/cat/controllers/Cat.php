<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Cat extends CI_Controller {

	public function __construct() {
		parent::__construct();
	}

	function index() {
		echo "this is cat controller";
	}
}
?>