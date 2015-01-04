//
//  IndexDataSource.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>
#import "IndexListModel.h"

@interface IndexDataSource : TTListDataSource<UIAlertViewDelegate>{
    IndexListModel* _indexListModel;
}
@property (nonatomic,retain) IndexListModel* indexListModel;
@end
