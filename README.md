<div align="center">
   <h1>SOUP</h1>
   <p>
      <b>단국대학교 캡스톤디자인(CE) SOUP</b>
   </p>
   <br>
</div>

<div align="center">

  ![Dart](https://img.shields.io/badge/Dart-0175C2?style=flat-square&logo=Dart&logoColor=white)
  ![Flutter](https://img.shields.io/badge/Flutter-02569B?style=flat-square&logo=Flutter&logoColor=white)

  ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white)
  ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white)
  ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=Spring-Boot&logoColor=white)
  ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=Spring-Security&logoColor=white)
  ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat-square&logo=Hibernate&logoColor=white)
  ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white)
  ![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)

  ![Naver Cloud Platform](https://img.shields.io/badge/Naver%20Cloud%20Platform-03C75A?style=flat-square&logo=Naver&logoColor=white)

</div>

## 1. 서비스 소개

### 1-1. SOUP란?

'SOUP(스프)'는 **스터디/프로젝트 그룹**을 만들어 멤버를 모집하고 **진행** 및 **관리**할 수 있는 플랫폼입니다. 

### 1-2. 주요 기능

- **스터디/프로젝트 생성**
  - 여러 옵션을 선택하여 그룹을 생성할 수 있습니다.

- **스터디/프로젝트 목록 조회**
  - 그룹 **전체 목록** 중 필터링 기능을 통해 원하는 그룹을 조회할 수 있습니다.
  - 그룹 진행 상태(진행 전, 진행 중, 진행 완료)에 따라 **참여 중인** 그룹 목록을 조회할 수 있습니다.

- **멤버 모집 및 참여 수락**
  - 그룹 생성 시, 입력한 모집 옵션(바로 수락, 직접 선택)에 따라 멤버를 수락할 수 있습니다.
    - **바로 수락시**, 그룹 주최자가 입력한 인원수에 도달하면 모집이 자동으로 마감됩니다.
    - **직접 선택시**, 그룹 주최자가 신청 멤버 목록(신청자의 프로필과 메세지)을 확인하고 참여 수락 또는 거절을 할 수 있습니다.

- **그룹원 리뷰**
  - 참여 중이었던 그룹이 종료되면, 그룹원끼리 리뷰를 할 수 있습니다.

### 1-3. 부가 기능
- **그룹/사용자 검색**
- **그룹 찜**
- **팔로우** 

<br />

## 2. 서비스 구조

### 2-1. 서비스 구조 설계

<table>
    <tr align="center">
        <td>
            <strong>Before (Micro Service Architecture)</strong>
        </td>
        <td>
            <strong>After (Monolithic Architecture)</strong>
        </td>
    </tr>
    <tr align="center">
        <td>
            <img src="https://user-images.githubusercontent.com/81179951/212279319-d140963c-5046-43dc-a05c-b52fed700bc8.png" width="100%" height="100%" />
        </td>
        <td>
            <img src="https://user-images.githubusercontent.com/81179951/212281220-dfc50b93-8e48-4733-bc32-bb34b194626c.png" width="100%" height="100%" />
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <strong>Micro Service Architecture 에서 Monolithic Architecture 로 전환한 이유</strong>
            <p>
            처음에는 Micro Service Architecture를 채택하여 인증 서버와 서비스 서버의 개발 및 배포 작업을 완료했습니다. 하지만 서버 자원과 비용 문제로 인해 Monolithic Architecture로 전환하게 되었습니다.</p>
        </td>
    </tr>
</table>

### 2-2. Database Schema
![DB](https://user-images.githubusercontent.com/81179951/212294856-5d061348-4859-45cc-a796-9d895107454c.png)

<br />

## 3. 구현 모습
### 3-1. 메인
#### 3-1-1. 인증
- 회원가입을 통해 사용자의 사진과 이름, 닉네임 정보를 받습니다.
- 카카오 로그인을 제공합니다.
<img src="https://user-images.githubusercontent.com/81179951/213639680-b7992574-72d3-4c3e-adb2-adf370e46b14.gif" width="250">

#### 3-1-2. 홈 화면
- 팀원을 모집하는 글들을 조회수를 기준으로 상위 10개의 그룹을 보여줍니다.
<img src="https://user-images.githubusercontent.com/81179951/213639740-84dcdcaf-3bd7-462a-bff9-7d20cc77114d.png" width="250">

#### 3-1-3. 검색
- 그룹명(키워드)로 그룹을 검색할 수 있습니다. 
- 사용자의 닉네임으로 사용자를 검색할 수 있습니다. 
<img src="https://user-images.githubusercontent.com/81179951/213640025-3e1e22ad-2bae-4ac1-898b-b26b18f662ec.gif" width="250">

### 3-2. 참여 목록
#### 3-2-1. 참여중인 스터디&프로젝트 목록
- 스터디 & 프로젝트 그룹 상태 준비 중과 진행 중, 진행 완료로 필터링하여 사용자가 참여 중인 그룹의 목록을 볼 수 있습니다.
  - 준비 중 : 팀장이 그룹을 생성하고 멤버를 모집하는 상태.
  - 진행 중 : 그룹 활동이 시작된 상태.
  - 진행 완료 : 그룹 활동이 종료된 상태. 멤버 평가 가능.
<img src="https://user-images.githubusercontent.com/81179951/213640138-239e1ebd-ad6f-4a3c-a656-8d3f3831cc9a.gif" width="250">

#### 3-2-2. 참여중인 그룹 상세보기
- 참여 중인 그룹의 공지를 확인할 수 있습니다.
- 참여 중인 그룹의 주차별 내용을 확인할 수 있습니다.
- 참여 중인 그룹에 질문을 작성하고 자유롭게 답변을 남기며 소통할 수 있습니다.
<img src="https://user-images.githubusercontent.com/81179951/213640409-a3816955-37a6-41b1-9782-5dbe01cd31c6.gif" width="250">

#### 3-2-3. 참여중인 그룹 멤버 목록 확인
- 참여 중인 그룹의 멤버 목록을 확인할 수 있습니다.

<img src="https://user-images.githubusercontent.com/81179951/213640831-5dcbbaea-3c87-4b82-b493-571007aaf077.png" width="250"> <img src="https://user-images.githubusercontent.com/81179951/213640845-8ab3eef2-f0e8-45e0-a81d-49b5ff37c475.png" width="250">

### 3-3. 모든 목록
#### 3-3-1. 모든 스터디&프로젝트 목록
- 모든 스터디와 프로젝트를 보여줍니다.
- 필터링을 통해 그룹 상태와 진행 방식을 선택할 수 있습니다.
  - 그룹 상태 : 준비 중, 진행 중, 진행 완료
  - 진행 방식 : 온라인, 오프라인
<img src="https://user-images.githubusercontent.com/81179951/213640524-825b949a-4856-41f1-aae6-64570be7b464.gif" width="250">

#### 3-3-2. 스터디&프로젝트 신청
- 참여하고자 하는 그룹을 선택하고 '참여하기' 버튼을 누르면 참여 신청이 완료됩니다.
- 팀장에게 하고 싶은 말을 자유롭게 작성하여 해당 내용과 함께 신청할 수 있습니다.
<img src="https://user-images.githubusercontent.com/81179951/213641212-26070e0c-7806-47ed-a8a7-1545f0ae9b34.gif" width="250">

#### 3-3-3. 그룹 생성
- 그룹 상세 설정을 통해 그룹 분류, 진행 방식, 모집 방식 등의 옵션 선택이 가능합니다.
<img src="https://user-images.githubusercontent.com/81179951/213640609-a550b246-6e29-4320-8ebe-0e230526b270.gif" width="250">

#### 3-3-4. 멤버 참여 수락
- 팀장이 신청 목록을 확인하고 원하는 멤버만을 선택하여 참여를 수락할 수 있습니다.
<img src="https://user-images.githubusercontent.com/81179951/213640747-32ba694d-2688-4719-92d5-f4904926c1d9.gif" width="250">

### 3-4. 프로필
#### 3-4-1. 내 프로필 
- 사용자의 팔로우, 팔로잉, 스크랩 목록, 참여 중인 SOUP 목록을 확인할 수 있습니다.
<img src="https://user-images.githubusercontent.com/81179951/213641286-679c04c7-3c6c-4479-a764-89a854b24dcd.png" width="250">

#### 3-4-2. 그룹 찜
- 추후에 스터디나 프로젝트 그룹에 참여 신청을 하는데 도움이 되도록 그룹을 찜할 수 있습니다. 
<img src="https://user-images.githubusercontent.com/81179951/213641328-12843fff-a1b2-4eaa-a353-cb839339c454.gif" width="250">

#### 3-4-3. 팔로우 
- 사용자끼리 팔로우, 팔로잉 관계를 맺고 목록을 확인할 수 있습니다. 
<img src="https://user-images.githubusercontent.com/81179951/213641133-e14f5a53-0953-4bbc-9171-c56d58c81233.gif" width="250">

#### 3-4-4. 멤버 리뷰
- 참여 중이었던 스터디와 프로젝트가 종료되면, 그룹원끼리 리뷰를 남길 수 있습니다.
<img src="https://user-images.githubusercontent.com/81179951/213641789-06c8b5cc-def6-4f42-a108-358156c75ffe.gif" width="250">

<br />

## 4. 팀원

<table align="center">
  <tr align="center">
    <td>
        <img src="https://avatars.githubusercontent.com/u/65934968?v=4" width=160px height=160px alt="himitery" />
    </td>
    <td>
        <img src="https://avatars.githubusercontent.com/u/81179951?v=4" width=160px height=160px alt="govl6113" />
    </td>
    <td>
        <img src="https://user-images.githubusercontent.com/81179951/213647484-8f331919-c734-4fb9-bde5-4a8649a8674a.png" width=160px height=160px alt="rldudtj" />
    </td>
  </tr>    
  <tr align="center">
    <td>
      <a href="https://github.com/himitery">
        <span>이학진</span>
      </a>
    </td>
    <td>
      <a href="https://github.com/govl6113">
        <span>배지현</span>
      </a>
    </td> 
    <td>
      <a href="https://github.com/rldudtj">
        <span>기영서</span>
      </a>
    </td>
  </tr>
  <tr align="center">
    <td>
      <span>Server & Application</span>
    </td>
    <td>
      <span>Server</span>
    </td>
    <td>
      <span>Design</span>
    </td>
  </tr>
</table>
