import React, { useState, useEffect } from 'react';
import { Download, Eye } from 'lucide-react';
import { Payslip } from '@/types';
import { payslipAPI } from '@/services/api';
import toast from 'react-hot-toast';

const PayslipPage: React.FC = () => {
  const [payslips, setPayslips] = useState<Payslip[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedPayslip, setSelectedPayslip] = useState<Payslip | null>(null);

  useEffect(() => {
    fetchPayslips();
  }, []);

  const fetchPayslips = async () => {
    try {
      setLoading(true);
      // In a real app, this would use the current employee's ID
      // const response = await payslipAPI.getByEmployee(employeeId);
      // setPayslips(response.data.content || []);
      setPayslips([]);
    } catch (error) {
      toast.error('Failed to fetch payslips');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Payslips</h1>
        <p className="text-gray-600 mt-2">View and download your payslips</p>
      </div>

      {/* Payslips List */}
      <div className="card">
        {loading ? (
          <div className="text-center py-8">Loading...</div>
        ) : payslips.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-gray-500 mb-4">No payslips available yet</p>
          </div>
        ) : (
          <div className="space-y-4">
            {payslips.map((payslip) => (
              <div
                key={payslip.id}
                className="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
              >
                <div>
                  <p className="font-semibold text-gray-900">
                    Payslip for {payslip.payrollMonth}
                  </p>
                  <p className="text-sm text-gray-600">
                    Net Pay: <span className="font-semibold text-green-600">${payslip.netPay}</span>
                  </p>
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => setSelectedPayslip(payslip)}
                    className="btn-secondary flex items-center"
                  >
                    <Eye className="w-4 h-4 mr-2" />
                    View
                  </button>
                  <button className="btn-primary flex items-center">
                    <Download className="w-4 h-4 mr-2" />
                    Download
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Payslip Details Modal */}
      {selectedPayslip && (
        <div className="card">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-semibold">Payslip Details</h3>
            <button onClick={() => setSelectedPayslip(null)} className="text-gray-500">×</button>
          </div>

          <div className="grid grid-cols-2 gap-4 mb-6">
            <div>
              <label className="text-sm text-gray-600">Period</label>
              <p className="font-semibold">{selectedPayslip.payrollMonth}</p>
            </div>
            <div>
              <label className="text-sm text-gray-600">Employee</label>
              <p className="font-semibold">{selectedPayslip.employeeName}</p>
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <h4 className="font-semibold text-gray-900 mb-3">Earnings</h4>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-gray-600">Base Pay</span>
                  <span>${selectedPayslip.basePay}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Overtime Pay</span>
                  <span>${selectedPayslip.overtimePay}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Bonus</span>
                  <span>${selectedPayslip.bonus}</span>
                </div>
                <div className="flex justify-between font-semibold text-green-600 border-t border-gray-200 pt-2">
                  <span>Total Earnings</span>
                  <span>${selectedPayslip.totalEarnings}</span>
                </div>
              </div>
            </div>

            <div>
              <h4 className="font-semibold text-gray-900 mb-3">Deductions</h4>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-gray-600">Tax</span>
                  <span>${selectedPayslip.taxDeduction}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Other Deductions</span>
                  <span>${selectedPayslip.otherDeductions}</span>
                </div>
                <div className="flex justify-between font-semibold text-red-600 border-t border-gray-200 pt-2">
                  <span>Total Deductions</span>
                  <span>${selectedPayslip.totalDeductions}</span>
                </div>
              </div>
            </div>

            <div className="bg-blue-50 p-4 rounded-lg">
              <div className="flex justify-between items-center">
                <span className="text-lg font-semibold text-gray-900">Net Pay</span>
                <span className="text-2xl font-bold text-blue-600">${selectedPayslip.netPay}</span>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PayslipPage;

