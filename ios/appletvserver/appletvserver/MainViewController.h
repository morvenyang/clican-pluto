//
//  MainViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Three20/Three20.h>
#import "MBProgressHUD.h"

@interface MainViewController : TTTableViewController<UITableViewDelegate,MBProgressHUDDelegate>{
    MBProgressHUD* _progressHUD;
}
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@end
