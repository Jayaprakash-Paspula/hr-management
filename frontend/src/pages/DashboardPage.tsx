import React, { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { Users, DollarSign, Clock, TrendingUp } from 'lucide-react';
import { DashboardStats } from '@/types';

const DashboardPage: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats>({
    totalEmployees: 0,
    totalPayroll: 0,
    pendingApprovals: 0,
    monthlyExpense: 0,
  });

  // Mock data for charts
  const monthlyExpenseData = [
    { month: 'Jan', amount: 45000 },
    { month: 'Feb', amount: 52000 },
    { month: 'Mar', amount: 48000 },
    { month: 'Apr', amount: 61000 },
    { month: 'May', amount: 55000 },
    { month: 'Jun', amount: 67000 },
  ];

  const departmentData = [
    { name: 'Engineering', value: 35, color: '#3B82F6' },
    { name: 'HR', value: 15, color: '#10B981' },
    { name: 'Sales', value: 25, color: '#F59E0B' },
    { name: 'Marketing', value: 25, color: '#EF4444' },
  ];

  useEffect(() => {
    // In a real app, fetch from API
    setStats({
      totalEmployees: 124,
      totalPayroll: 892000,
      pendingApprovals: 8,
      monthlyExpense: 67000,
    });
  }, []);

  const StatCard: React.FC<{ title: string; value: string; icon: React.ReactNode; color: string }> = ({
    title,
    value,
    icon,
    color,
  }) => (
    <div className="card">
      <div className="flex items-center">
        <div className={`p-3 rounded-full ${color}`}>
          {icon}
        </div>
        <div className="ml-4">
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-2xl font-bold text-gray-900">{value}</p>
        </div>
      </div>
    </div>
  );

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600 mt-2">Welcome to HR Management System</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Total Employees"
          value={stats.totalEmployees.toString()}
          icon={<Users className="w-6 h-6 text-white" />}
          color="bg-blue-500"
        />
        <StatCard
          title="Total Payroll"
          value={`$${stats.totalPayroll.toLocaleString()}`}
          icon={<DollarSign className="w-6 h-6 text-white" />}
          color="bg-green-500"
        />
        <StatCard
          title="Pending Approvals"
          value={stats.pendingApprovals.toString()}
          icon={<Clock className="w-6 h-6 text-white" />}
          color="bg-yellow-500"
        />
        <StatCard
          title="Monthly Expense"
          value={`$${stats.monthlyExpense.toLocaleString()}`}
          icon={<TrendingUp className="w-6 h-6 text-white" />}
          color="bg-purple-500"
        />
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Monthly Expense Chart */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Monthly Expenses</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={monthlyExpenseData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip formatter={(value) => [`$${value}`, 'Amount']} />
              <Bar dataKey="amount" fill="#3B82F6" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Department Distribution */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Department Distribution</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={departmentData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {departmentData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={entry.color} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Activity</h3>
        <div className="space-y-4">
          <div className="flex items-center justify-between py-2 border-b border-gray-200">
            <div>
              <p className="text-sm font-medium text-gray-900">John Doe submitted timesheet</p>
              <p className="text-xs text-gray-500">2 hours ago</p>
            </div>
            <span className="px-2 py-1 text-xs bg-blue-100 text-blue-800 rounded-full">Timesheet</span>
          </div>
          <div className="flex items-center justify-between py-2 border-b border-gray-200">
            <div>
              <p className="text-sm font-medium text-gray-900">Salary processed for March</p>
              <p className="text-xs text-gray-500">1 day ago</p>
            </div>
            <span className="px-2 py-1 text-xs bg-green-100 text-green-800 rounded-full">Payroll</span>
          </div>
          <div className="flex items-center justify-between py-2">
            <div>
              <p className="text-sm font-medium text-gray-900">New employee onboarded</p>
              <p className="text-xs text-gray-500">3 days ago</p>
            </div>
            <span className="px-2 py-1 text-xs bg-purple-100 text-purple-800 rounded-full">Employee</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;

