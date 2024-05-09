export interface SearchIconProps {
  className?: string;
}

export default function SearchIcon({ className }: SearchIconProps) {
  return (
    <svg
      className={`stroke-icon ${className}`}
      width="16"
      height="16"
      viewBox="0 0 16 16"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M15 15L10.3333 10.3333M11.8889 6.44444C11.8889 7.15942 11.7481 7.86739 11.4745 8.52794C11.2008 9.18849 10.7998 9.78868 10.2942 10.2942C9.78868 10.7998 9.18849 11.2008 8.52794 11.4745C7.86739 11.7481 7.15942 11.8889 6.44444 11.8889C5.72947 11.8889 5.0215 11.7481 4.36095 11.4745C3.7004 11.2008 3.1002 10.7998 2.59464 10.2942C2.08908 9.78868 1.68804 9.18849 1.41443 8.52794C1.14082 7.86739 1 7.15942 1 6.44444C1 5.00049 1.57361 3.61567 2.59464 2.59464C3.61567 1.57361 5.00049 1 6.44444 1C7.8884 1 9.27322 1.57361 10.2942 2.59464C11.3153 3.61567 11.8889 5.00049 11.8889 6.44444Z"
        stroke="current"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}
