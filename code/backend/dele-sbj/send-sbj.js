const fs = require('fs');
const xlsx = require('xlsx');

try {
    const data = fs.readFileSync('./sbj.json', 'utf8');
    const jsonData = JSON.parse(data);
    console.log(jsonData);

    const allSbj = [];
    for (let stream in jsonData) {
        let streamObj = jsonData[stream];
        for (let sem in streamObj) {
            let semesterObj = jsonData[stream][sem];
            for (let subjectType in semesterObj) {
                let subjectObj = semesterObj[subjectType];
                let totalSubjectsH = null;
                let totalSubjectsTmp = null;
                let totalSubjectsG = null;

                if (semesterObj["elective"].length === 0) {
                    if (Number(sem.charAt(sem.length - 1) > 4))
                    totalSubjectsH = semesterObj["common"].length + semesterObj["honours"].length;
                    totalSubjectsG = semesterObj["common"].length + semesterObj["general"].length;
                }
                else {
                    totalSubjectsTmp = stream.toUpperCase() !== "BCOM" ? 4 : (stream.toUpperCase() === "BCOM" && Number(sem.charAt(sem.length - 1) < 4)) ? 5 : 4
                }

                for (let s = 0; s < subjectObj.length; s++) {
                    let sbj = {
                        stream: stream.toUpperCase(),
                        semester: Number(sem.charAt(sem.length - 1)),
                        subjectType: subjectObj[s].startsWith("AECC") ? "SPECIAL" : subjectType.toUpperCase(),
                        subjectName: subjectObj[s],
                        credit: subjectObj[s].includes("AECC") ? 2 : 6,
                        fullMarks: 100,
                        totalSubjects: totalSubjectsH || totalSubjectsG || totalSubjectsTmp
                    }
                    // console.log(sbj)

                    allSbj.push(sbj);
                }
            }
        }
    }

    // Convert the array of objects to a worksheet
    const ws = xlsx.utils.json_to_sheet(allSbj);

    // Create a new workbook
    const wb = xlsx.utils.book_new();

    // Append the worksheet to the workbook
    xlsx.utils.book_append_sheet(wb, ws, 'Subjects');

    // Write the workbook to a file
    xlsx.writeFile(wb, 'subjects.xlsx');

    console.log('Data written to subjects.xlsx');

} catch (err) {
    console.error("An error occurred:", err);
}
