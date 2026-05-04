import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Menu, Home, Users, Clock, CalculatorIcon, FileText, LogOut } from 'lucide-react';
import { useAuth } from '@/context/AuthContext';
import { authAPI } from '@/services/api';

const Sidebar: React.FC = () => {
  const location = useLocation();
  const { user, logout } = useAuth();
  const [isOpen, setIsOpen] = React.useState(true);

  const handleLogout = () => {
    authAPI.logout();
    logout();
    window.location.href = '/login';
  };

  const menuItems = [
    { path: '/', label: 'Dashboard', icon: Home },
    { path: '/employees', label: 'Employees', icon: Users },
    { path: '/timesheet', label: 'Timesheet', icon: Clock },
    { path: '/compensation-calculator', label: 'CRC Calculator', icon: CalculatorIcon },
    { path: '/payslips', label: 'Payslips', icon: FileText },
  ];

  const isActive = (path: string) => location.pathname === path;

  return (
    <div className={`${isOpen ? 'w-64' : 'w-20'} bg-gray-900 text-white transition-all duration-300 flex flex-col`}>
      {/* Logo */}
      <div className="p-4 border-b border-gray-800">
        <div className="flex items-center justify-between">
          {isOpen && <span className="font-bold text-lg">HRMS</span>}
          <button
            onClick={() => setIsOpen(!isOpen)}
            className="p-2 hover:bg-gray-800 rounded-lg"
          >
            <Menu className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* Menu Items */}
      <nav className="flex-1 p-4 space-y-2">
        {menuItems.map(({ path, label, icon: Icon }) => (
          <Link
            key={path}
            to={path}
            className={`flex items-center space-x-3 px-3 py-2 rounded-lg transition-colors ${
              isActive(path)
                ? 'bg-blue-600 text-white'
                : 'text-gray-400 hover:bg-gray-800'
            }`}
          >
            <Icon className="w-5 h-5" />
            {isOpen && <span>{label}</span>}
          </Link>
        ))}
      </nav>

      {/* User Profile & Logout */}
      <div className="p-4 border-t border-gray-800 space-y-2">
        {isOpen && (
          <div className="text-xs text-gray-400 truncate">
            <p className="font-medium text-white">{user?.username}</p>
            <p>{user?.role}</p>
          </div>
        )}
        <button
          onClick={handleLogout}
          className="w-full flex items-center space-x-2 px-3 py-2 text-red-400 hover:bg-gray-800 rounded-lg transition-colors"
        >
          <LogOut className="w-5 h-5" />
          {isOpen && <span>Logout</span>}
        </button>
      </div>
    </div>
  );
};

export default Sidebar;



