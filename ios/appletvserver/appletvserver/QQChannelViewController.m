//
//  QQChannelViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-22.
//
//

#import "QQChannelViewController.h"
#import "Constants.h"
@implementation QQChannelViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (void)createModel {
    NSMutableArray* items = [NSMutableArray array];
    for(NSNumber* qqChannelNumber in [Constants qqChannelArray]){
        QQChannel qqChannel = [qqChannelNumber intValue];
        TTTableTextItem* item = [TTTableTextItem itemWithText:[Constants qqChannelConvertToString:qqChannel] URL:[@"atvserver://qq/index/" stringByAppendingFormat:@"%i",qqChannel]];
        [items addObject:item];
    }
    
    TTListDataSource* ds = [[TTListDataSource alloc] initWithItems:items];
    self.dataSource = ds;
}

- (void)loadView
{
    [super loadView];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
