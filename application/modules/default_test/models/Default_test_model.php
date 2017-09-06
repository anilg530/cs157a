<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class Default_test_model extends CI_Model {
	
	function __construct() {
		parent::__construct();
	}
	
	function get_users() {
		$query = "
		SELECT *
		FROM user
		";

		$result = $this->db->query($query);
		if ($result->num_rows() > 0) {
			return $result->result_array();
		}
		else {
			return array();
		}
	}
}