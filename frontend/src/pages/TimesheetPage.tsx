import React, { useState } from 'react';
import { Plus, Send } from 'lucide-react';
import { Timesheet } from '@/types';
import { timesheetAPI } from '@/services/api';
import toast from 'react-hot-toast';

const TimesheetPage: React.FC = () => {
  const [timesheets, setTimesheets] = useState<Timesheet[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    timesheetDate: new Date().toISOString().split('T')[0],
    hoursWorked: '',
    project: '',
    taskDescription: '',
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // Handle submit logic here
    toast.success('Timesheet entry created');
    setShowForm(false);
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Timesheet</h1>
          <p className="text-gray-600 mt-2">Track your daily work hours</p>
        </div>
        <button
          onClick={() => setShowForm(!showForm)}
          className="btn-primary flex items-center"
        >
          <Plus className="w-4 h-4 mr-2" />
          New Entry
        </button>
      </div>

      {/* Form */}
      {showForm && (
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">New Timesheet Entry</h3>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Date</label>
                <input
                  type="date"
                  name="timesheetDate"
                  value={formData.timesheetDate}
                  onChange={handleInputChange}
                  className="input-field"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Hours Worked</label>
                <input
                  type="number"
                  step="0.5"
                  name="hoursWorked"
                  value={formData.hoursWorked}
                  onChange={handleInputChange}
                  placeholder="8.0"
                  className="input-field"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Project</label>
                <input
                  type="text"
                  name="project"
                  value={formData.project}
                  onChange={handleInputChange}
                  placeholder="Project name"
                  className="input-field"
                />
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Task Description</label>
              <textarea
                name="taskDescription"
                value={formData.taskDescription}
                onChange={handleInputChange}
                placeholder="What did you work on?"
                rows={4}
                className="input-field"
              />
            </div>
            <div className="flex gap-3">
              <button type="submit" className="btn-primary flex items-center">
                <Send className="w-4 h-4 mr-2" />
                Save Entry
              </button>
              <button
                type="button"
                onClick={() => setShowForm(false)}
                className="btn-secondary"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Entries */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Your Timesheets</h3>
        {timesheets.length === 0 ? (
          <div className="text-center py-8 text-gray-500">No timesheet entries yet</div>
        ) : (
          <div className="space-y-4">
            {timesheets.map((timesheet) => (
              <div key={timesheet.id} className="border border-gray-200 rounded-lg p-4">
                <div className="flex justify-between items-start">
                  <div>
                    <p className="font-semibold text-gray-900">{timesheet.project}</p>
                    <p className="text-sm text-gray-600">{timesheet.timesheetDate} - {timesheet.hoursWorked} hours</p>
                    <p className="text-sm text-gray-500 mt-2">{timesheet.taskDescription}</p>
                  </div>
                  <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                    timesheet.status === 'APPROVED' ? 'bg-green-100 text-green-800' :
                    timesheet.status === 'SUBMITTED' ? 'bg-blue-100 text-blue-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {timesheet.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default TimesheetPage;

