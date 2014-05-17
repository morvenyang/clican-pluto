//
//  LoginDataSource.m
//  hpsdf-ngp-mudole-iphone-client
//
//  Created by zhang wei on 11-12-6.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import "LoginDataSource.h"


@implementation LoginDataSource

- (UITableViewCell*)tableView:(UITableView *)tableView
        cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell* cell = [super tableView:tableView cellForRowAtIndexPath:indexPath];
    cell.backgroundColor = [UIColor blackColor];
    cell.textLabel.textColor = RGBCOLOR(102, 102, 102);
    return cell;
}
@end
