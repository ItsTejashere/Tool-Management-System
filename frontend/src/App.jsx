import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import AddTool from './pages/AddTool';
import ToolDetails from './pages/ToolDetails';
import EditTool from './pages/EditTool';
import ProjectSelection from './pages/ProjectSelection';
import PlantSelection from './pages/PlantSelection';
import DepartmentSelection from './pages/DepartmentSelection';


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/add-tool" element={<AddTool />} />
        <Route path="/tool/:id" element={<ToolDetails />} />
        <Route path="/edit-tool/:id" element={<EditTool />} />
        <Route path="/plant-selection" element={<PlantSelection />} />
        <Route path="/department-selection" element={<DepartmentSelection />} />
        <Route path="/project-selection" element={<ProjectSelection />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;