import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";

const Success = () => {
  const [params] = useSearchParams();
  const tranId = params.get("tran_id");

  useEffect(() => {
    console.log("Payment succeeded. Tran ID:", tranId);
    // optionally trigger backend confirmation or show success toast
  }, [tranId]);

  return (
    <div className="p-10 text-center">
      <h1 className="text-3xl font-bold text-green-700">Payment Successful!</h1>
      <p className="mt-4">Transaction ID: {tranId}</p>
    </div>
  );
};

export default Success;
