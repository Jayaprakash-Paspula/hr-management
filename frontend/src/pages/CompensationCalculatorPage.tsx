import React, { useState } from 'react';
import { Calculator, DollarSign, TrendingUp, Receipt } from 'lucide-react';
import { CompensationCalculatorRequest, CompensationCalculatorResponse } from '@/types';
import { compensationAPI } from '@/services/api';
import toast from 'react-hot-toast';

const CompensationCalculatorPage: React.FC = () => {
  const [formData, setFormData] = useState<CompensationCalculatorRequest>({
    baseSalary: '50000',
    hourlyRate: '25',
    hoursWorked: '160',
    overtimeMultiplier: '1.5',
    bonusPercentage: '5',
    taxPercentage: '10',
    otherDeductions: '0',
  });

  const [result, setResult] = useState<CompensationCalculatorResponse | null>(null);
  const [loading, setLoading] = useState(false);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleCalculate = async () => {
    setLoading(true);
    try {
      const response = await compensationAPI.calculate(formData);
      setResult(response.data);
      toast.success('Calculation completed successfully!');
    } catch (error) {
      toast.error('Failed to calculate compensation');
    } finally {
      setLoading(false);
    }
  };

  const handlePreview = async () => {
    setLoading(true);
    try {
      const response = await compensationAPI.preview(formData);
      setResult(response.data);
    } catch (error) {
      toast.error('Failed to preview calculation');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Compensation Rate Calculator (CRC)</h1>
        <p className="text-gray-600 mt-2">Calculate employee pay with real-time breakdown</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Calculator Form */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-6 flex items-center">
            <Calculator className="w-5 h-5 mr-2" />
            Calculator Inputs
          </h3>

          <div className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Base Salary ($)</label>
                <input
                  type="number"
                  name="baseSalary"
                  value={formData.baseSalary}
                  onChange={handleInputChange}
                  className="input-field"
                  placeholder="50000"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Hourly Rate ($)</label>
                <input
                  type="number"
                  step="0.01"
                  name="hourlyRate"
                  value={formData.hourlyRate}
                  onChange={handleInputChange}
                  className="input-field"
                  placeholder="25.00"
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
                  className="input-field"
                  placeholder="160"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Overtime Multiplier</label>
                <input
                  type="number"
                  step="0.1"
                  name="overtimeMultiplier"
                  value={formData.overtimeMultiplier}
                  onChange={handleInputChange}
                  className="input-field"
                  placeholder="1.5"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Bonus (%)</label>
                <input
                  type="number"
                  step="0.1"
                  name="bonusPercentage"
                  value={formData.bonusPercentage}
                  onChange={handleInputChange}
                  className="input-field"
                  placeholder="5"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Tax (%)</label>
                <input
                  type="number"
                  step="0.1"
                  name="taxPercentage"
                  value={formData.taxPercentage}
                  onChange={handleInputChange}
                  className="input-field"
                  placeholder="10"
                />
              </div>
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-gray-700">Other Deductions ($)</label>
                <input
                  type="number"
                  step="0.01"
                  name="otherDeductions"
                  value={formData.otherDeductions}
                  onChange={handleInputChange}
                  className="input-field"
                  placeholder="0.00"
                />
              </div>
            </div>

            <div className="flex space-x-3 pt-4">
              <button
                onClick={handlePreview}
                disabled={loading}
                className="btn-secondary flex items-center"
              >
                <TrendingUp className="w-4 h-4 mr-2" />
                Preview
              </button>
              <button
                onClick={handleCalculate}
                disabled={loading}
                className="btn-primary flex items-center"
              >
                <Calculator className="w-4 h-4 mr-2" />
                {loading ? 'Calculating...' : 'Calculate'}
              </button>
            </div>
          </div>
        </div>

        {/* Results Panel */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-6 flex items-center">
            <Receipt className="w-5 h-5 mr-2" />
            Compensation Breakdown
          </h3>

          {result ? (
            <div className="space-y-4">
              {/* Earnings */}
              <div className="border-b border-gray-200 pb-4">
                <h4 className="font-medium text-gray-900 mb-3">Earnings</h4>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="text-sm text-gray-600">Base Pay:</span>
                    <span className="text-sm font-medium">${result.basePay}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-sm text-gray-600">Overtime Pay:</span>
                    <span className="text-sm font-medium">${result.overtimePay}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-sm text-gray-600">Bonus:</span>
                    <span className="text-sm font-medium">${result.bonus}</span>
                  </div>
                  <div className="flex justify-between font-semibold text-green-600 border-t border-gray-200 pt-2">
                    <span>Total Earnings:</span>
                    <span>${result.totalEarnings}</span>
                  </div>
                </div>
              </div>

              {/* Deductions */}
              <div className="border-b border-gray-200 pb-4">
                <h4 className="font-medium text-gray-900 mb-3">Deductions</h4>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="text-sm text-gray-600">Tax Deduction:</span>
                    <span className="text-sm font-medium">${result.taxDeduction}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-sm text-gray-600">Other Deductions:</span>
                    <span className="text-sm font-medium">${result.otherDeductions}</span>
                  </div>
                  <div className="flex justify-between font-semibold text-red-600 border-t border-gray-200 pt-2">
                    <span>Total Deductions:</span>
                    <span>${result.totalDeductions}</span>
                  </div>
                </div>
              </div>

              {/* Net Pay */}
              <div className="bg-blue-50 p-4 rounded-lg">
                <div className="flex justify-between items-center">
                  <span className="text-lg font-semibold text-gray-900">Net Pay:</span>
                  <span className="text-2xl font-bold text-blue-600">${result.netPay}</span>
                </div>
              </div>

              {/* Additional Info */}
              <div className="text-xs text-gray-500 space-y-1">
                <p>Hours Worked: {result.hoursWorked}</p>
                <p>Overtime Hours: {result.overtimeHours}</p>
                <p>Regular Hours: {result.regularHours}</p>
                <p>Hourly Rate: ${result.hourlyRate}</p>
                <p>Overtime Rate: ${result.overtimeRate}</p>
              </div>
            </div>
          ) : (
            <div className="text-center py-12 text-gray-500">
              <Calculator className="w-12 h-12 mx-auto mb-4 opacity-50" />
              <p>Enter values and click Calculate to see the breakdown</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CompensationCalculatorPage;

