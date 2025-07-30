import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";

export default function PaymentSuccess() {
  const [searchParams] = useSearchParams();
  const tranId = searchParams.get("tran_id");

  useEffect(() => {
    document.title = "Payment Success";
  }, []);

  return (
    <div className="min-h-screen flex items-center justify-center bg-green-50 p-4">
      <div className="bg-white p-8 rounded shadow-md text-center">
        <h2 className="text-2xl font-bold text-green-700 mb-4">🎉 Payment Successful!</h2>
        <p className="text-gray-700 mb-2">Thank you for your payment.</p>
        {tranId && <p className="text-gray-600">Transaction ID: <strong>{tranId}</strong></p>}
      </div>
    </div>
  );
}
