import React from 'react';
import { Loader2 } from 'lucide-react';

const LoadingSpinner = () => (
  <div className="min-h-screen flex flex-col justify-center items-center">
    <div className="glass-effect rounded-3xl p-8 shadow-2xl border border-white/20">
      <div className="flex flex-col items-center space-y-4">
        <div className="relative">
          <Loader2 className="animate-spin text-blue-600" size={48} />
          <div className="absolute inset-0 rounded-full border-4 border-blue-200 animate-pulse"></div>
        </div>
        <div className="text-center">
          <h3 className="text-lg font-semibold text-gray-900 mb-1">Loading...</h3>
          <p className="text-sm text-gray-600">Please wait while we prepare your experience</p>
        </div>
      </div>
    </div>
  </div>
);

export default LoadingSpinner;
