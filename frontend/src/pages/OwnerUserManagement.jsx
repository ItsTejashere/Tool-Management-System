import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import API_URL from '../config';

export default function OwnerUserManagement() {
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');

  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [plants, setPlants] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [selectedPlantId, setSelectedPlantId] = useState('');
  const [selectedDeptId, setSelectedDeptId] = useState('');
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    role: '',
    plantId: '',
    deptId: '',
    email: ''
  });
  const [message, setMessage] = useState(null);

  useEffect(() => {
    if (userRole !== 'OWNER') {
      navigate('/dashboard');
      return;
    }

    const fetchData = async () => {
      try {
        const [usersRes, rolesRes, plantsRes] = await Promise.all([
          axios.get(`${API_URL}/api/users`),
          axios.get(`${API_URL}/api/users/roles`),
          axios.get(`${API_URL}/api/plants`)
        ]);
        setUsers(usersRes.data);
        setRoles(rolesRes.data);
        setPlants(plantsRes.data);
      } catch (error) {
        console.error('Failed loading owner management data', error);
      }
    };

    fetchData();
  }, [navigate, userRole]);

  useEffect(() => {
    if (!selectedPlantId) {
      setDepartments([]);
      setSelectedDeptId('');
      setFormData((prev) => ({ ...prev, deptId: '' }));
      return;
    }

    const fetchDepartments = async () => {
      try {
        const res = await axios.get(`${API_URL}/api/departments/${selectedPlantId}`);
        setDepartments(res.data);
      } catch (error) {
        console.error('Failed to load departments', error);
      }
    };

    fetchDepartments();
  }, [selectedPlantId]);

  const refreshUsers = async () => {
    const res = await axios.get(`${API_URL}/api/users`);
    setUsers(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.username || !formData.password || !formData.role || !formData.email) {
      setMessage({ type: 'danger', text: 'Please fill in all required fields.' });
      return;
    }

    try {
      const payload = {
        ...formData,
        plantId: formData.plantId ? parseInt(formData.plantId) : null,
        deptId: formData.deptId ? parseInt(formData.deptId) : null
      };

      const response = await axios.post(`${API_URL}/api/users`, payload);
      if (response.data.status) {
        setMessage({ type: 'success', text: response.data.message });
        setFormData({ username: '', password: '', role: '', plantId: '', deptId: '', email: '' });
        setSelectedPlantId('');
        setSelectedDeptId('');
        refreshUsers();
      } else {
        setMessage({ type: 'danger', text: response.data.message || 'Failed to add user.' });
      }
    } catch (error) {
      console.error(error);
      setMessage({ type: 'danger', text: 'Failed to add user.' });
    }
  };

  const handleDelete = async (userId) => {
    if (!window.confirm('Are you sure you want to remove this user?')) return;

    try {
      const response = await axios.delete(`${API_URL}/api/users/${userId}`);
      if (response.data.status) {
        setMessage({ type: 'success', text: response.data.message });
        refreshUsers();
      } else {
        setMessage({ type: 'danger', text: response.data.message || 'Failed to delete user.' });
      }
    } catch (error) {
      console.error(error);
      setMessage({ type: 'danger', text: 'Failed to delete user.' });
    }
  };

  return (
    <div className="container-fluid bg-light min-vh-100 py-5">
      <div className="w-100" style={{ maxWidth: '1100px', margin: '0 auto' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <h2 className="fw-bold text-primary mb-1">Owner User Management</h2>
            <p className="text-muted">Create, manage, and remove users with plant and department assignment.</p>
          </div>
          <div>
            <button className="btn btn-outline-secondary me-2" onClick={() => navigate('/dashboard')}>Back to Dashboard</button>
            <button className="btn btn-danger" onClick={() => { localStorage.clear(); navigate('/login'); }}>Logout</button>
          </div>
        </div>

        {message && (
          <div className={`alert alert-${message.type} rounded-3`}>
            {message.text}
          </div>
        )}

        <div className="row gy-4">
          <div className="col-lg-5">
            <div className="card shadow-sm border-0 rounded-4 p-4 bg-white">
              <h4 className="fw-bold mb-3">Add New User</h4>
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Username</label>
                  <input type="text" className="form-control bg-light" value={formData.username} onChange={(e) => setFormData({ ...formData, username: e.target.value })} required />
                </div>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Password</label>
                  <input type="password" className="form-control bg-light" value={formData.password} onChange={(e) => setFormData({ ...formData, password: e.target.value })} required />
                </div>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Role</label>
                  <select className="form-select bg-light" value={formData.role} onChange={(e) => setFormData({ ...formData, role: e.target.value })} required>
                    <option value="">Select Role</option>
                    {roles.map((role) => (
                      <option key={role} value={role}>{role}</option>
                    ))}
                  </select>
                </div>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Plant</label>
                  <select className="form-select bg-light" value={formData.plantId} onChange={(e) => { setSelectedPlantId(e.target.value); setFormData({ ...formData, plantId: e.target.value, deptId: '' }); }}>
                    <option value="">Select Plant</option>
                    {plants.map((plant) => (
                      <option key={plant.plantId} value={plant.plantId}>{plant.plantName}</option>
                    ))}
                  </select>
                </div>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Department</label>
                  <select className="form-select bg-light" value={formData.deptId} onChange={(e) => setFormData({ ...formData, deptId: e.target.value })} disabled={!selectedPlantId}>
                    <option value="">Select Department</option>
                    {departments.map((dept) => (
                      <option key={dept.departmentId} value={dept.departmentId}>{dept.departmentName}</option>
                    ))}
                  </select>
                </div>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Email</label>
                  <input type="email" className="form-control bg-light" value={formData.email} onChange={(e) => setFormData({ ...formData, email: e.target.value })} required />
                </div>
                <button type="submit" className="btn btn-primary w-100 rounded-pill">Create User</button>
              </form>
            </div>
          </div>

          <div className="col-lg-7">
            <div className="card shadow-sm border-0 rounded-4 p-4 bg-white">
              <h4 className="fw-bold mb-3">Existing Users</h4>
              <div className="table-responsive">
                <table className="table table-hover align-middle">
                  <thead className="table-light">
                    <tr>
                      <th>Username</th>
                      <th>Role</th>
                      <th>Plant</th>
                      <th>Department</th>
                      <th>Email</th>
                      <th className="text-end">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user) => (
                      <tr key={user.id}>
                        <td>{user.username}</td>
                        <td>{user.role}</td>
                        <td>{user.plantId || '-'}</td>
                        <td>{user.deptId || '-'}</td>
                        <td>{user.email || '-'}</td>
                        <td className="text-end">
                          <button className="btn btn-sm btn-danger" onClick={() => handleDelete(user.id)}>
                            Remove
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
