// types/gtag.d.ts

export {};

declare global {
  interface Window {
    gtag: (...args: any[]) => void;
  }

  function gtag(...args: any[]): void;
}
