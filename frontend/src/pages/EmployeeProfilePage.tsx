import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Employee } from '@/types';
import { employeeAPI } from '@/services/api';
import toast from 'react-hot-toast';

const EmployeeProfilePage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      fetchEmployee();
    }
  }, [id]);

  const fetchEmployee = async () => {
    try {
      setLoading(true);
      const response = await employeeAPI.getById(id!);
      setEmployee(response.data);
    } catch (error) {
      toast.error('Failed to fetch employee details');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-center py-8">Loading...</div>;
  }

  if (!employee) {
    return <div className="text-center py-8">Employee not found</div>;
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Employee Profile</h1>
        <p className="text-gray-600 mt-2">View and manage employee information</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Profile Card */}
        <div className="card">
          <div className="text-center">
            <div className="w-24 h-24 bg-gray-300 rounded-full mx-auto mb-4 flex items-center justify-center">
              <span className="text-2xl font-bold text-gray-700">
                {employee.firstName.charAt(0)}{employee.lastName.charAt(0)}
              </span>
            </div>
            <h2 className="text-xl font-semibold text-gray-900">
              {employee.firstName} {employee.lastName}
            </h2>
            <p className="text-gray-600">{employee.designation}</p>
            <p className="text-sm text-gray-500">{employee.departmentName}</p>
            <div className="mt-4">
              <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                employee.active
                  ? 'bg-green-100 text-green-800'
                  : 'bg-red-100 text-red-800'
              }`}>
                {employee.active ? 'Active' : 'Inactive'}
              </span>
            </div>
          </div>
        </div>

        {/* Details */}
        <div className="lg:col-span-2 space-y-6">
          <div className="card">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Personal Information</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Email</label>
                <p className="mt-1 text-sm text-gray-900">{employee.email}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Phone</label>
                <p className="mt-1 text-sm text-gray-900">{employee.phone || 'N/A'}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Joining Date</label>
                <p className="mt-1 text-sm text-gray-900">{employee.joiningDate}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Manager</label>
                <p className="mt-1 text-sm text-gray-900">{employee.managerName || 'N/A'}</p>
              </div>
            </div>
          </div>

          <div className="card">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Salary Information</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Base Salary</label>
                <p className="mt-1 text-sm text-gray-900">${employee.baseSalary}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Hourly Rate</label>
                <p className="mt-1 text-sm text-gray-900">
                  {employee.hourlyRate ? `$${employee.hourlyRate}` : 'N/A'}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Overtime Rate</label>
                <p className="mt-1 text-sm text-gray-900">
                  {employee.overtimeRate ? `${employee.overtimeRate}x` : 'N/A'}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmployeeProfilePage;

