import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Button } from "@/components/ui/button"; // assuming you're using Shadcn UI

const Success = () => {
  const [params] = useSearchParams();
  const tranId = params.get("tran_id");
  const navigate = useNavigate();

  useEffect(() => {
    console.log("Payment succeeded. Tran ID:", tranId);
    // Optionally trigger backend confirmation or show success toast
  }, [tranId]);

  return (
    <div className="p-10 text-center space-y-6">
      <h1 className="text-3xl font-bold text-green-700">Payment Successful!</h1>
      <p className="text-lg">Transaction ID: <span className="font-mono">{tranId}</span></p>

      <Button
        className="mt-4 bg-green-600 hover:bg-green-700"
        onClick={() => navigate("/dashboard")}
      >
        Back to Dashboard
      </Button>
    </div>
  );
};

export default Success;
