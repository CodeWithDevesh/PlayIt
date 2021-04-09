#include <string>
#include <direct.h>

using namespace std;

int main(int arg_count, char* arg_list[]) {

	string exeName(arg_list[0]);
	string rootDir = exeName.substr(0, exeName.find_last_of("\\"));
	string jarFile = ".\\dist\\PlayIt.jar";
	string cmd = "cd " + rootDir;
	string javaCmdFile = ".\\jre\\bin\\javaw.exe";

	_chdir(rootDir.c_str());

	if (arg_count == 1) {
		cmd = "start " + javaCmdFile + " -jar " + jarFile + " visible";
	}
	else if (arg_count > 1) {
		cmd = "start " + javaCmdFile + " -jar " + jarFile + " \"invisible\"";
		for (int i = 1; i < arg_count; i++) {
			cmd += " \"" + string(arg_list[i]) + "\"";
		}
	}
	system(cmd.c_str());
}