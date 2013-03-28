//
//  MainViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Three20/Three20.h>
#import "MBProgressHUD.h"
#import <AssetsLibrary/AssetsLibrary.h>

@interface MainViewController : TTTableViewController<UITableViewDelegate,MBProgressHUDDelegate>{
    MBProgressHUD* _progressHUD;
    NSMutableArray* _assets;
    ALAssetsLibrary* _library;
}
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@property (nonatomic, retain) NSMutableArray    *assets;
@property (nonatomic, retain) ALAssetsLibrary    *library;
@end
