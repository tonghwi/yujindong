package com.wedding.weddinggiftai.analyzeApi;

import com.wedding.weddinggiftai.gpt.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AnalyzeApiService {
    private final AnalyzeApiRepository analyzeApiRepository;
    private final GptService gptService;

    public String extractMessageOnly(String kakaoText) {
        // 정규식: [이름] [시간] 제거
        return kakaoText.replaceAll("\\[.*?] \\[.*?] ", "");
    }

    public ChatResponse analyzeWithSummary(String text) {
        String cleanText = extractMessageOnly(text);
        if (cleanText.length() > 1000) {
            cleanText = cleanText.substring(cleanText.length() - 1000);
        }
        String prompt = """
                        다음은 두 사람의 카카오톡 대화 내용입니다.
                        이 대화를 바탕으로 두 사람의 친밀도를 0부터 100 사이의 숫자로 한 줄에만 숫자로 답해주세요.
                        
                        그 뒤에는 친밀도 판단 근거를 세 줄 요약해 주세요. 예시는 다음과 같습니다:
                        
                        85
                        - 자주 대화함
                        - 감정 표현이 많음
                        - 유머가 있음
                        
                        대화:
                        """ + cleanText;
        String answer = gptService.askGpt(prompt);
        System.out.println("GPT 응답: " + answer);

        int score = 50;
        String summary="";

        try {
            String[] lines = answer.split("\n");
            score = Integer.parseInt(lines[0].replaceAll("[^0-9]", ""));
            if (lines.length > 1) {
                summary = String.join("\n", Arrays.copyOfRange(lines, 1, lines.length));
            }
        } catch (Exception e) {
            summary = "요약을 불러오지 못했어요.";
        }

        String recommendation = getRecommendation(score);

        return new ChatResponse(score, recommendation, true, "분석완료", summary);

    }

    public String getRecommendation(int score) {
        if (score <= 20) return "0원";
        if (score <= 40) return "5만원";
        if (score <= 60) return "7만원";
        if (score <= 80) return "10만원";
        if (score <= 95) return "15만원";
        return "20만원 이상";
    }

    public void SaveAnalyzeResult(String ip,String text,ChatResponse response){
        AnalyzeApi analyzeApi = new AnalyzeApi();
        analyzeApi.setIp(ip);
        analyzeApi.setText(text);
        analyzeApi.setScore(response.getScore());
        analyzeApi.setRecommendation(response.getRecommendation());
        analyzeApi.setSummary(response.getSummary());
        analyzeApi.setCreatedAt(LocalDateTime.now());
        analyzeApiRepository.save(analyzeApi);
    }

}
