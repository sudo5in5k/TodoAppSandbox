###
# github setting
###

# Ignore inline messages which lay outside a diff's range of PR
github.dismiss_out_of_range_messages

# Show a warning when work in progress
if github.pr_title.include? "[WIP]" || github.pr_labels.include?("WIP")
  warn("PR is classed as Work in Progress")
end

###
# for PR
###

# Show a warning when there is a big pull request
warn("This is a large PR") if git.lines_of_code > 500


# Show a warning when PR has no milestone
warn("Please set a milestone")


# Show a warning when PR has no assignees
warn("Please set some assignees")

###
# ktlint
###
checkstyle_format.base_path = Dir.pwd
checkstyle_format.report 'app/build/reports/ktlint/ktlintMainSourceSetCheck.xml'