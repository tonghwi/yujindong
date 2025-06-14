import { useAnalyzeStore } from "@/store/AnalyzeStore";

export default function TextArea() {
  const { text, setText } = useAnalyzeStore();
  return (
    <textarea
      value={text}
      onChange={(e) => setText(e.target.value)}
      className="w-[280px] h-[150px] border-2 p-4 bg-white text-sm lg:w-[400px] lg:h-[400px] lg:text-base"
      placeholder="분석할 내용을 입력해 주세요"
    />
  );
}
