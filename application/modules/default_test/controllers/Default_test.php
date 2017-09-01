<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Default_test extends CI_Controller {

	public function __construct() {
		parent::__construct();
	}

	function index() {
		$this->load->view('default_page');
	}
}
?>