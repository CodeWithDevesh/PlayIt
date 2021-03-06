#include <Windows.h>
#include <direct.h>
#include <iostream>
#include <processthreadsapi.h>
#include <string>
#include <SFML/System.hpp>
#include <SFML/Network.hpp>

using namespace std;

int main(int arg_count, char* arg_list[])
{

	sf::Http http("http://localhost/", 2021);
	sf::Http::Request request("/open", sf::Http::Request::Put);
	//request.setMethod(sf::Http::Request::Get);
	//request.setUri("/");
	if (arg_count > 1)
		request.setField("Path", arg_list[1]);
	sf::Http::Response response = http.sendRequest(request);

	if (response.getStatus() != sf::Http::Response::Ok) {
		string exeName(arg_list[0]);
		string rootDir = exeName.substr(0, exeName.find_last_of("\\"));
		string jarFile = ".\\dist\\PlayIt.jar";
		string cmd = "cd " + rootDir;
		string javaCmdFile = ".\\jre\\bin\\javaw.exe";

		_chdir(rootDir.c_str());

		if (arg_count == 1) {
			cmd = " -jar " + jarFile;
		}
		else if (arg_count > 1) {
			cmd = " -jar " + jarFile;
			for (int i = 1; i < arg_count; i++) {
				cmd += " \"" + string(arg_list[i]) + "\"";
			}
		}

		STARTUPINFO si;
		PROCESS_INFORMATION pi;

		ZeroMemory(&si, sizeof(si));
		si.cb = sizeof(si);
		ZeroMemory(&pi, sizeof(pi));

		CreateProcess((LPCSTR)javaCmdFile.c_str(),   // No module name (use command line)
			(LPSTR)cmd.c_str(),        // Command line
			NULL,           // Process handle not inheritable
			NULL,           // Thread handle not inheritable
			FALSE,          // Set handle inheritance to FALSE
			CREATE_NO_WINDOW,              // No creation flags
			NULL,           // Use parent's environment block
			NULL,           // Use parent's starting directory 
			&si,            // Pointer to STARTUPINFO structure
			&pi)            // Pointer to PROCESS_INFORMATION structure
			;
	}
}
