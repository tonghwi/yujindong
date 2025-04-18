import { useMutation } from "@tanstack/react-query";

function useAuthMutation<T>(path: string) {
  const BASE_URL = process.env.NEXT_PUBLIC_API_URL!;

  return useMutation({
    mutationFn: (formData: T) =>
      fetch(`${BASE_URL}/${path}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      }),
    onSuccess: async (res) => {
      const data = await res.text();
      console.log(`${BASE_URL}/${path} 성공!`, data);
    },
    onError: (err) => {
      console.error(`${BASE_URL}/${path} 실패 😢`, err);
    },
  });
}

export default useAuthMutation;
