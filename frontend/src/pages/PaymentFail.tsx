export default function PaymentFail() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-red-50 p-4">
      <div className="bg-white p-8 rounded shadow-md text-center">
        <h2 className="text-2xl font-bold text-red-700 mb-4">❌ Payment Failed</h2>
        <p className="text-gray-700">Sorry, something went wrong with your payment.</p>
      </div>
    </div>
  );
}
