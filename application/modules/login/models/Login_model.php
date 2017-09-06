<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class Login_model extends CI_Model {
	public function __construct() {
		parent::__construct();
	}

	function is_logged_in() {
		$username = $this->session->userdata('username');
		if (!empty($username)) {
			return true;
		}
		else {
			return false;
		}
	}

	function get_logged_in_username() {
		return $this->session->userdata('username');
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