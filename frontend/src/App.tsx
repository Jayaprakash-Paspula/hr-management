import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from '@/context/AuthContext';
import LoginPage from '@/pages/LoginPage';
import DashboardPage from '@/pages/DashboardPage';
import EmployeeListPage from '@/pages/EmployeeListPage';
import EmployeeProfilePage from '@/pages/EmployeeProfilePage';
import TimesheetPage from '@/pages/TimesheetPage';
import CompensationCalculatorPage from '@/pages/CompensationCalculatorPage';
import PayslipPage from '@/pages/PayslipPage';
import Sidebar from '@/components/Sidebar';
import Header from '@/components/Header';

// Protected Route Component
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};

// Main Layout Component
const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-50 p-6">
          {children}
        </main>
      </div>
    </div>
  );
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            {/* Public Routes */}
            <Route path="/login" element={<LoginPage />} />

            {/* Protected Routes */}
            <Route
              path="/"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <DashboardPage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <DashboardPage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/employees"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <EmployeeListPage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/employees/:id"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <EmployeeProfilePage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/timesheet"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <TimesheetPage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/compensation-calculator"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <CompensationCalculatorPage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/payslips"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <PayslipPage />
                  </MainLayout>
                </ProtectedRoute>
              }
            />

            {/* Default redirect */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
        <Toaster position="top-right" />
      </Router>
    </AuthProvider>
  );
}

export default App;

